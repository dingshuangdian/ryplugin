/********* RongCloudNav.m Cordova Plugin Implementation *******/

#import "RongCloudNav.h"
#import "RCManage.h"
#import "RQService.h"
#import "RCChatListViewController.h"
#import "RCRConversationViewController.h"

@implementation RongCloudNav

- (void)startWithHost:(CDVInvokedUrlCommand *)command {
    
    NSDictionary* host = [command.arguments objectAtIndex:0];
    [RQService shareInstance].HOST = host;
    
    NSString *RCAppKey = [host objectForKey:@"RCAppKey"];
    
    if (RCAppKey) {
        [[RCManage shareInstance] configInfo:RCAppKey];
    }
}

- (void)connectWithToken:(CDVInvokedUrlCommand *)command {
    __block CDVPluginResult* pluginResult = nil;
    NSString *token = @"";
    NSString *RCToken = @"";
    NSDictionary *connectDic = [command argumentAtIndex:0];
    if (connectDic) {
        if ([connectDic objectForKey:@"tokenId"]) {
            token = [connectDic objectForKey:@"tokenId"];
        }
        if ([connectDic objectForKey:@"RCtokenId"]) {
            RCToken = [connectDic objectForKey:@"RCtokenId"];
        }
    }
    
    [RQService shareInstance].token = token;
    
    [[RCManage shareInstance] connectWithToken:RCToken success:^(NSString *userId) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:userId];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    } failure:^(NSDictionary *error) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:error];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)disconnect:(CDVInvokedUrlCommand *)command {
    [[RCManage shareInstance] RCMdisconnect];
}

- (void)pushChatListView:(CDVInvokedUrlCommand *)command {
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
       RCChatListViewController *chatListView = [[RCChatListViewController alloc] init];
        UINavigationController *rootView = [[UINavigationController alloc] initWithRootViewController:chatListView];
        [self.viewController presentViewController:rootView animated:YES completion:nil];
    });
}

- (void)pushConversionView:(CDVInvokedUrlCommand *)command {
    
    NSString *targetId = @"";
    NSString *conversationTitle = @"";
    NSDictionary *conversationDic = [command argumentAtIndex:0];
    if (conversationDic) {
        targetId = [NSString stringWithFormat:@"%@",[conversationDic objectForKey:@"targetId"]];
        conversationTitle = [conversationDic objectForKey:@"conversationTitle"];
    }
    dispatch_async(dispatch_get_main_queue(), ^{
        RCRConversationViewController *conversationVC = (RCRConversationViewController *)[[RCRConversationViewController alloc] initWithConversationType:ConversationType_GROUP targetId:targetId];
        conversationVC.title = conversationTitle;
        conversationVC.enableNewComingMessageIcon = YES; //开启消息提醒
        conversationVC.enableUnreadMessageIcon = YES;
        
        UINavigationController *rootView = [[UINavigationController alloc] initWithRootViewController:conversationVC];
        
        [self.viewController presentViewController:rootView animated:YES completion:nil];
    });
    
}


//获取当前ViewController
- (UIViewController *)topViewController {
    UIViewController *resultVC;
    resultVC = [self _topViewController:[[UIApplication sharedApplication].keyWindow rootViewController]];
    while (resultVC.presentedViewController) {
        resultVC = [self _topViewController:resultVC.presentedViewController];
    }
    return resultVC;
}

- (UIViewController *)_topViewController:(UIViewController *)vc {
    if ([vc isKindOfClass:[UINavigationController class]]) {
        return [self _topViewController:[(UINavigationController *)vc topViewController]];
    } else if ([vc isKindOfClass:[UITabBarController class]]) {
        return [self _topViewController:[(UITabBarController *)vc selectedViewController]];
    } else {
        return vc;
    }
    return nil;
}

@end
