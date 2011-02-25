//
//  ANTLRInputStream.m
//  ANTLR
//
//  Created by Alan Condit on 2/21/11.
//  Copyright 2011 Alan's MachineWorks. All rights reserved.
//

#import <Cocoa/Cocoa.h>
#import "ANTLRInputStream.h"


@implementation ANTLRInputStream

+ (id) newANTLRInputStream
{
    return [[ANTLRInputStream alloc] init];
}

+ (id) newANTLRInputStream:(NSFileHandle *)anInput
{
    return [[ANTLRInputStream alloc] initWithInput:anInput size:ANTLRReaderStream.INITIAL_BUFFER_SIZE readBufferSize:ANTLRReaderStream.READ_BUFFER_SIZE encoding:NSASCIIStringEncoding];
}

+ (id) newANTLRInputStream:(NSFileHandle *)anInput size:(NSInteger)theSize
{
    return [[ANTLRInputStream alloc] initWithInput:anInput size:theSize readBufferSize:ANTLRReaderStream.READ_BUFFER_SIZE encoding:NSASCIIStringEncoding];
}

+ (id) newANTLRInputStream:(NSFileHandle *)anInput encoding:(NSStringEncoding)theEncoding
{
    return [[ANTLRInputStream alloc] initWithInput:anInput size:ANTLRReaderStream.INITIAL_BUFFER_SIZE readBufferSize:ANTLRReaderStream.READ_BUFFER_SIZE encoding:theEncoding];
}

+ (id) newANTLRInputStream:(NSFileHandle *)anInput
                      size:(NSInteger)theSize
            readBufferSize:(NSInteger)theRBSize
                  encoding:(NSStringEncoding)theEncoding
{
    return [[ANTLRInputStream alloc] initWithInput:anInput size:theSize readBufferSize:theRBSize encoding:theEncoding];
}

- (id) init
{
    if (self = [super init]) {
    }
    return self;
}

- (id) initWithInput:(NSFileHandle *)anInput
                size:(NSInteger)theSize
      readBufferSize:(NSInteger)theRBSize
            encoding:(NSStringEncoding)theEncoding
{
    NSData *myData;
    NSFileHandle *isr;
    NSURL *aURL;
    NSError *error;
    if ( self = [super init] ) {
        myData = [NSData data];
        if ( theEncoding != 0 ) {
            isr = [NSFileHandle fileHandleForReadingFromURL:aURL error:&error];
        }
        else {
            isr = [NSFileHandle fileHandleForReadingFromURL:aURL error:&error];
        }
        [self load:isr size:theSize readBufferSize:theRBSize];
    }
    return self;
}

@end
