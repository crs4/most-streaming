//
//  main.m
//  VideoTest01
//
//  Created by Francesco Cabras on 23/05/14.
//  Copyright (c) 2014 Francesco Cabras. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "GTTAppDelegate.h"
#include "gst_ios_init.h"

int main(int argc, char * argv[])
{
    @autoreleasepool {
        gst_ios_init();
        return UIApplicationMain(argc, argv, nil, NSStringFromClass([GTTAppDelegate class]));
    }
}
