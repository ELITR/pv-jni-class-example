package it.pervoice.eubridge.mcloud.jni;

import it.pervoice.eubridge.mcloud.MCloudException;

import java.util.Date;

/**
 * Class defining an EUBridge Service Architecture audio packet.
 *
 * @author Andrea Zito
 */
public class MCloudAudioPacket extends MCloudDataPacket {

  private byte[] samples;
  private Boolean isFinal;

  /**
   * Construct a new audio packet.
   *
   * @param startTime Human readable starting timestamp
   * @param stopTime Human readable ending timestamp
   * @param timeOffset Time offset in MS relative to the beginning of the stream
   * @param fingerPrint Fingerprint of the text
   * @param samples Samples to be included in this packet
   * @param isFinal Indicates whether this packet is the last one
   */
  public MCloudAudioPacket(Date startTime, Date stopTime, int timeOffset, String fingerPrint, byte[] samples, boolean isFinal) {
    super(MCloudPacket.PacketType.DATA_AUDIO, startTime, stopTime, timeOffset, fingerPrint);

    this.samples = samples;
    this.isFinal = isFinal;
  }

  /**
   * Constructs a new MCloud audio packet from a native instance
   */
  MCloudAudioPacket(long nativeInstancePtr, long mcloudNativeInstancePtr) {
    super(MCloudPacket.PacketType.DATA_AUDIO, nativeInstancePtr, mcloudNativeInstancePtr);
  }

  /**
   * Retrieves the audio chunk contained in this packet
   *
   * @return audio bytes
   */
  public byte[] getAudio() {
    if (samples == null) {
      if (bindingStatus == MCloudPacket.BindingStatus.BINDED) {
        samples = getAudioImpl(nativeInstancePtr, mcloudNativeInstancePtr);
      } else {
        throw new IllegalStateException("Cannot retrieve values for unbinded packet!");
      }
    }

    return samples;
  }

  @Override
  protected long bindToNativeInstanceImpl(MCloudBase mcloud) throws MCloudException {
    if (samples == null) throw new MCloudException("Missing audio samples");
    if (isFinal == null) throw new MCloudException("Missing audio final flag");

    Date startTime = getStartTime();
    Date stopTime = getStopTime();
    String fingerPrint = getFingerPrint();

    if (startTime == null) throw new MCloudException("Missing start time");
    if (stopTime == null) throw new MCloudException("Missing stop time");
    if (fingerPrint == null) throw new MCloudException("Missing fingerprint");

    return bindToNativeInstanceNativeImpl(mcloud.getNativeInstancePointer(), dateFormat.format(startTime), dateFormat.format(stopTime), fingerPrint, samples, isFinal);
  }

  /* ************************************************************
   * Native method declarations
   **************************************************************/
 
  /**
   * Native implementation of bindToNativeInstanceImpl
   *
   * @param mcloudNativeInstancePtr Pointer to the native instance of MCloud
   * @param samples Samples of this audio packet
   * @param isFinal True if this packet is final
   * @return Native instance pointer
   */
  private native long bindToNativeInstanceNativeImpl(long mcloudNativeInstancePtr, String startTime, String stopTime, String fingerPrint, byte[] samples, boolean isFinal);
 
  /**
   * Native implementation of getAudio
   *
   * @param nativeInstacePtr Pointer to the native instance of this packet
   * @param mcloudNativeInstancePtr Pointer to the native instance of MCloud backing this object
   */
  private native byte[] getAudioImpl(long nativeInstancePtr, long mcloudNativeInstancePtr);
}