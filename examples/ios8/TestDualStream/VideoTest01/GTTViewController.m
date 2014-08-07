/*!
 * Project MOST - Moving Outcomes to Standard Telemedicine Practice
 * http://most.crs4.it/
 *
 * Copyright 2014, CRS4 srl. (http://www.crs4.it/)
 * Dual licensed under the MIT or GPL Version 2 licenses.
 * See license-GPLv2.txt or license-MIT.txt
 */
#import "GTTViewController.h"
#import "GStreamerBackend.h"

@interface GTTViewController ()
{
    int media_width;
    int media_height;
    NSString *camera_uri;
    NSString *encoder_uri;
    GStreamerBackend *camera_backend;
    GStreamerBackend *encoder_backend;
}


@end

@implementation GTTViewController

- (void)viewDidLoad
{
    [super viewDidLoad];

    media_width = 586;
    media_height = 480;
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"urls" ofType:@"plist"];
    NSDictionary *dict = [NSDictionary dictionaryWithContentsOfFile:path];
    
    camera_uri = dict[@"camera_uri"];
    encoder_uri = dict[@"encoder_uri"];
    camera_backend = [[GStreamerBackend alloc] init:self videoView:self.cameraView];
    encoder_backend = [[GStreamerBackend alloc] init:self videoView:self.encoderView];
    
}


-(void)viewWillDisappear:(BOOL)animated
{
    if(camera_backend && encoder_backend){
        [camera_backend deinit];
        [encoder_backend deinit];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


/*
 * Methods from GstreamerBackendDelegate
 */

#pragma mark - GstreamerBackendDelegate

-(void) gstreamerInitialized
{
    dispatch_async(dispatch_get_main_queue(), ^{
//        play_button.enabled = TRUE;
//        pause_button.enabled = TRUE;
//        message_label.text = @"Ready";
        [camera_backend setUri:camera_uri];
        [encoder_backend setUri:encoder_uri];
//        is_local_media = [uri hasPrefix:@"file://"];
//        is_playing_desired = NO;
        
        [camera_backend play];
        [encoder_backend play];
        
    });
}

-(void) gstreamerSetUIMessage:(NSString *)message
{
    dispatch_async(dispatch_get_main_queue(), ^{
        NSLog(@"%@", message);
    });
}

-(void) mediaSizeChanged:(NSInteger)width height:(NSInteger)height
{
    media_width = width;
    media_height = height;
    dispatch_async(dispatch_get_main_queue(), ^{
//        [self viewDidLayoutSubviews];
//        [video_view setNeedsLayout];
//        [video_view layoutIfNeeded];
    });
}

-(void) setCurrentPosition:(NSInteger)position duration:(NSInteger)duration
{
//    /* Ignore messages from the pipeline if the time sliders is being dragged */
//    if (dragging_slider) return;
//    
//    dispatch_async(dispatch_get_main_queue(), ^{
//        time_slider.maximumValue = duration;
//        time_slider.value = position;
//        [self updateTimeWidget];
//    });
}

@end
