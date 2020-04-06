package com.bado.ignacio.safefield


@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
public annotation class Safe {

}


/**
 * It will keep any class after proguard minify
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class Keep
