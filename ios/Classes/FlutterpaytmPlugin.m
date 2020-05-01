#import "FlutterpaytmPlugin.h"
#if __has_include(<flutterpaytm/flutterpaytm-Swift.h>)
#import <flutterpaytm/flutterpaytm-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutterpaytm-Swift.h"
#endif

@implementation FlutterpaytmPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterpaytmPlugin registerWithRegistrar:registrar];
}
@end
