
#ifndef BBAUDIO_H
#define BBAUDIO_H

#include "../gxruntime/StringType.h"
#include "bbsys.h"
#include "../gxruntime/gxaudio.h"

extern gxAudio *gx_audio;

gxSound *	 bbLoadSound( const String& file );
void		 bbFreeSound( gxSound *sound );
gxChannel *	 bbPlaySound( gxSound *sound );
void		 bbLoopSound( gxSound *sound );
void		 bbSoundPitch( gxSound *sound,int pitch );
void		 bbSoundVolume( gxSound *sound,float volume );
void		 bbSoundPan( gxSound *sound,float pan );
void		 bbStopChannel( gxChannel *channel );
void		 bbPauseChannel( gxChannel *channel );
void		 bbResumeChannel( gxChannel *channel );
void		 bbChannelPitch( gxChannel *channel,int pitch );
void		 bbChannelVolume( gxChannel *channel,float volume );
void		 bbChannelPan( gxChannel *channel,float pan );
bool		 bbChannelPlaying( gxChannel *channel );
gxSound*     bbLoad3DSound(const String& f);

#endif

