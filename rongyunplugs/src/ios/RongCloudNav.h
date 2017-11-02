/********* RongCloudNav.m Cordova Plugin Implementation *******/

#import <Cordova/CDVPlugin.h>

@interface RongCloudNav : CDVPlugin {
  // Member variables go here.
}

- (void)startWithHost:(CDVInvokedUrlCommand*)command;

- (void)connectWithToken:(CDVInvokedUrlCommand *)command;

- (void)disconnect:(CDVInvokedUrlCommand *)command;

- (void)pushChatListView:(CDVInvokedUrlCommand *)command;

- (void)pushConversionView:(CDVInvokedUrlCommand *)command;
@end

