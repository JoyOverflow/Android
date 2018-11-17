package pxgd.hyena.com.androng;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class SoundManager
{
   static private SoundManager _instance;
   private static SoundPool mSoundPool;
   private static HashMap<Integer, Integer> mSoundPoolMap;
   private static AudioManager mAudioManager;
   private static Context mContext;
   private static boolean playSound;

   private SoundManager()
   {
      playSound = true;
   }

   static synchronized public SoundManager getInstance()
   {
      if (_instance == null)
         _instance = new SoundManager();
      return _instance;
   }

   public static void initSounds(Context theContext)
   {
      mContext = theContext;
      mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
      mSoundPoolMap = new HashMap<Integer, Integer>();
      mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
   }

   public static void loadSounds()
   {
      mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.hit, 1));
      mSoundPoolMap.put(2, mSoundPool.load(mContext, R.raw.terminator, 1));
      mSoundPoolMap.put(3, mSoundPool.load(mContext, R.raw.clap, 1));
   }

   public static void playSound(int index, float speed)
   {
      if (playSound)
      {
         float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
         streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
         mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, speed);
      }
   }

   public static void togglePlaySound()
   {
      playSound = !playSound;
      if (!playSound)
      {
         for (int soundIndex : mSoundPoolMap.values())
         {
            stopSound(soundIndex);
         }
      }
   }

   public static void stopSound(int index)
   {
      mSoundPool.stop(mSoundPoolMap.get(index));
   }

   public static void cleanup()
   {
      mSoundPool.release();
      mSoundPool = null;
      mSoundPoolMap.clear();
      mAudioManager.unloadSoundEffects();
      _instance = null;
   }
}