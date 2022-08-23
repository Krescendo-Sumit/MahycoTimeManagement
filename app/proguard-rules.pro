
#####################################Default proguard rules start

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.preference.Preference
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Dialog
-keep public class * extends android.widget.RelativeLayout
-keep public class * extends android.widget.ImageView

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembers class * {
 public protected *;
 public static final *;
 public static *;
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

-keepclassmembers class * extends androidx.appcompat.app.AppCompatActivity {
 public void *(android.view.View);
}

-keep class **.R$* {
    <fields>;
 }
-dontwarn javax.annotation.**
-ignorewarnings
##-keep class * {
##public private protected *;
##}
##-keep class * {*;}

-repackageclasses 'com.mahyco.time.timemanagement'

#####################################Default proguard rules ends