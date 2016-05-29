# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/nathanmittler/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# TODO: Figure out why AsmSchemaFactory doesn't work with obfuscation
-dontobfuscate

-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic

# Some annoying warnings.
-dontwarn sun.misc.Unsafe
-dontwarn javax.xml.bind.DatatypeConverter
-dontwarn android.test.AndroidTestCase
-dontwarn android.test.AndroidTestRunner
-dontwarn android.test.InstrumentationTestCase
-dontwarn android.test.InstrumentationTestSuite
-dontwarn android.test.suitebuilder.TestSuiteBuilder$FailedToCreateTests
-dontwarn junit.runner.FailureDetailView

# A couple of annoying notes.
-dontnote junit.runner.BaseTestRunner
-dontnote com.google.protobuf.experimental.util.UnsafeUtil

# Needed for annotation processing.
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

# Needed for looking up enum values by name.
-keep,allowoptimization enum * {
    *;
}
-keep class com.android.** {
    *;
}
#-keep class org.objectweb.asm.** {
#    *;
#}

