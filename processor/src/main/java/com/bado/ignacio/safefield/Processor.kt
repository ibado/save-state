package com.bado.ignacio.safefield

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import javax.annotation.processing.*
import javax.annotation.processing.Processor
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.util.Elements
import javax.tools.Diagnostic


@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor::class)
class Processor : AbstractProcessor() {

    companion object {
        const val TARGET = "target"
        const val BUNDLE_NAME: String = "bundle"
        const val SAVER_SUFFIX = "_Saver"
        val SAVER_INTERFACE = ClassName("com.bado.ignacio.statesaver", "Saver")
        val BUNDLE_CLASS = ClassName("android.os", "Bundle")
    }

    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elementUtils: Elements

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        super.init(processingEnvironment)
        processingEnvironment?.apply {
            this@Processor.filer = this.filer
            this@Processor.messager = this.messager
            this@Processor.elementUtils = this.elementUtils
        }
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {

        val typesToProcess =
            getTypeElementsToProcess(roundEnvironment?.rootElements!!, annotations!!)

        for (type in typesToProcess) {
            val packageName = elementUtils.getPackageOf(type).qualifiedName.toString()
            val activity = type.simpleName.toString()
            val typeName: String = activity + SAVER_SUFFIX

            val fields = mutableListOf<VariableElement>()
            for (element in type.enclosedElements) {

                if (element.kind == ElementKind.FIELD
                    && element.getAnnotation(Safe::class.java) != null) {

                    if (hasInvalidModifiers(element)) return true // stop the processing
                    fields += element as VariableElement
                }
            }

            val saveBuilder = FunSpec.builder("save")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(TARGET, ClassName(packageName, activity))
                .addParameter(BUNDLE_NAME, BUNDLE_CLASS)

            val restoreBuilder = FunSpec.builder("restore")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter(TARGET, ClassName(packageName, activity))
                .addParameter(BUNDLE_NAME, BUNDLE_CLASS)

            for (field in fields) {
                val data = BundleStatementGenerator.StatementData(
                    "${activity}_${field.simpleName}",
                    BUNDLE_NAME,
                    field.simpleName.toString(),
                    field.asType().toString(),
                    TARGET
                )
                saveBuilder.addStatement(BundleStatementGenerator.generatePutStatement(data))
                restoreBuilder.addStatement(BundleStatementGenerator.generateGetStatement(data))
            }

            val classBuilder = TypeSpec.classBuilder(typeName)
                .addAnnotation(Keep::class)
                .addSuperinterface(SAVER_INTERFACE.plusParameter(ClassName(packageName, activity)))
                .addFunction(saveBuilder.build())
                .addFunction(restoreBuilder.build())
                .build()

            FileSpec.builder(packageName, typeName)
                .addType(classBuilder)
                .build()
                .writeTo(filer)
        }

        return false
    }

    private fun hasInvalidModifiers(it: Element): Boolean {
        var hasInvalidModifiers = false
        if (it.modifiers.contains(Modifier.STATIC)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Field must not be static", it)
            hasInvalidModifiers = true
        }

        if (it.modifiers.contains(Modifier.FINAL)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Field must not be final", it)
            hasInvalidModifiers = true
        }

        if (it.modifiers.contains(Modifier.PRIVATE)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Field must not be private", it)
            hasInvalidModifiers = true
        }
        return hasInvalidModifiers
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            Safe::class.java.canonicalName,
            Keep::class.java.canonicalName
        )
    }

    private fun getTypeElementsToProcess(
        elements: Set<Element>,
        supportedAnnotations: Set<Element>
    ): MutableSet<TypeElement> {
        val typeElements = mutableSetOf<TypeElement>()
        for (element in elements) {
            if (element is TypeElement) {
                var found = false
                for (subElement in element.getEnclosedElements()) {
                    for (mirror in subElement.annotationMirrors) {
                        for (annotation in supportedAnnotations) {
                            if (mirror.annotationType.asElement() == annotation) {
                                typeElements.add(element)
                                found = true
                                break
                            }
                        }
                        if (found) break
                    }
                    if (found) break
                }
            }
        }
        return typeElements
    }
}
