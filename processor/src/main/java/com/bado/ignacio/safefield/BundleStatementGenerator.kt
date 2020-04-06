package com.bado.ignacio.safefield

import java.lang.Exception

object BundleStatementGenerator {

    fun generatePutStatement(data: StatementData): String {
        data.apply {
            val saveMethod = generatePutMethod(Type.of(fieldType))
            return "$className.$fieldName?.apply { $bundleName.$saveMethod(\"$tag\", this) }"
        }
    }

    fun generateGetStatement(data: StatementData): String {
        data.apply {
            val restoreMethod = generateGetMethod(Type.of(fieldType))
            return "$bundleName.$restoreMethod(\"$tag\")?.apply { $className.$fieldName = this}"
        }
    }

    data class StatementData(
        val tag: String,
        val bundleName: String,
        val fieldName: String,
        val fieldType: String,
        val className: String
    )

    private fun generatePutMethod(type: Type): String {
        return when (type) {
            Type.STRING -> "putString"
            Type.INT -> "putInt"
            Type.CHAR -> "putChar"
            Type.FLOAT -> "putFloat"
            Type.BOOLEAN -> "putBoolean"
            Type.DOUBLE -> "putDouble"
        }
    }

    private fun generateGetMethod(type: Type): String {
        return when (type) {
            Type.STRING -> "getString"
            Type.INT -> "getInt"
            Type.CHAR -> "getChar"
            Type.FLOAT -> "getFloat"
            Type.BOOLEAN -> "getBoolean"
            Type.DOUBLE -> "getDouble"
        }
    }

    sealed class Type() {
        object STRING : Type()
        object INT : Type()
        object CHAR : Type()
        object FLOAT : Type()
        object BOOLEAN : Type()
        object DOUBLE : Type()

        companion object {
            fun of(type: String): Type {
                return when (type) {
                    "java.lang.String" -> STRING
                    "java.lang.Integer", "int" -> INT
                    "java.lang.Character", "char" -> CHAR
                    "java.lang.Float", "float" -> FLOAT
                    "java.lang.Boolean", "boolean" -> BOOLEAN
                    "java.lang.Double", "double" -> DOUBLE
                    else -> throw NonSupportTypeException(type)
                }
            }
        }
    }

    class NonSupportTypeException(type: String) : Exception("$type type is not supported")

}