package it.pervoice.eubridge.mcloud.jni;

import it.pervoice.eubridge.mcloud.MCloudCodec;
import it.pervoice.eubridge.mcloud.MCloudDataType;
import it.pervoice.eubridge.mcloud.MCloudException;


/**
 * This class represent an MCloud Client
 * The client declares the output streams it want to be processed and request
 * the input streams it wants as result.
 *
 * @author Andrea Zito
 */
public class MCloudClient extends MCloudBase {

  /**
   * Creates a new client identified by the given name
   *
   * @param name Name of the client
   * @throws MCloudException Error creating native instance
   */
  public MCloudClient(String name) throws MCloudException {
    super(name, MCloudBase.MCloudMode.CLIENT);
  }
  
  /**
   * Creates a new client identified by the given name.
   * Using an SSL connection.
   *
   * @param name Name of the client
   * @param sslVerifyMode Verification mode of the server certificate 
   * @throws MCloudException Error creating native instance
   */
  public MCloudClient(String name, MCloudSSLVerifyMode sslVerifyMode) throws MCloudException {
    super(name, MCloudBase.MCloudMode.CLIENT, sslVerifyMode);
  }

  /**
   * Creates a new client identified by the given name, username and password
   *
   * @param name Name of the client
   * @paran username Account username
   * @param password Account password
   * @throws MCloudException Error creating native instance
   */
  public MCloudClient(String name, String username, String password) throws MCloudException {
    super(name, MCloudBase.MCloudMode.CLIENT, username, password);
  }
  
  /**
   * Allocates a new MCloud object in authorization mode.
   * @param name Name of the Component
   * @paran username Account username
   * @param password Account password
   * @param isHashed false if Account password is in plain text, true if encrypted using sha1 hash function
   * @throws MCloudException Error creating native instance
   */
  public MCloudClient(String name, String username, String password, boolean isHashed) throws MCloudException {
	super(name, MCloudBase.MCloudMode.CLIENT, username, password, isHashed);
  }
  
  /**
   * Allocates a new MCloud object in authorization mode with an SSL connection.
   * @param name Name of the Component
   * @paran username Account username
   * @param password Account password
   * @param isHashed false if Account password is in plain text, true if encrypted using sha1 hash function
   * @param sslEnabled false if use a plain connection, true if use a SSL connection 
   */
  public MCloudClient(String name, String username, String password, boolean isHashed, boolean sslEnabled, MCloudSSLVerifyMode sslVerifyMode) throws MCloudException {
	super(name, MCloudBase.MCloudMode.CLIENT, username, password, isHashed, sslEnabled, sslVerifyMode);
  }
  
  /**
   * Adds a flow description.
   * This method has to be called before connecting to the MCloud.
   * A client can add more than one flow being just translations of the same descriptions.
   * Therefore, the password and logging has to be the same over all flows.
   *
   * @param password An optional password which has to be used in order to subscribe to the flow
   *                 (display server), null for no password.
   * @param logging If false the flow will not be logged in the database
   * @param language Descriptive language identifier of the flow, e.g. English
   * @param name Name of the flow, e.g. title of a talk
   * @param description Additional description of the flow, e.g. abstract
   * @throws MCloudException Error happened
   */
  public void addFlowDescription(String password, boolean logging, String language, String name, String description) throws MCloudException {
    if (!addFlowDescriptionImpl(nativeInstancePtr, password, logging, language, name, description)) {
      throw new MCloudException("Error adding flow description");
    }
  }

  /**
   * Announce an output stream.
   * This method has to be called after the client has been connected to the MCloud.
   *
   * @param outputFingerPrint Fingerprint of the output stream
   * @param outputType Type of the output data
   * @param streamID Identifier of the stream
   * @param specifier Additional specifier, i.e. a speaker identifier
   * @throws MCloudException
   */
  public void announceOutputStream(String outputFingerPrint, MCloudDataType outputType, String streamID, String specifier) throws MCloudException {
    if (!announceOutputStreamImpl(nativeInstancePtr, outputFingerPrint, outputType.stringValue(), streamID, specifier)) {
      throw new MCloudException("Error announcing output stream");
    }
  }

  /**
   * Request an input stream.
   * This method has to be called in order to request a specific
   * input stream from the MCloud such as ASR or MT results. Otherwise,
   * the client will not receive any data.
   * This method has to be called after the client has been connected to the MCloud.
   *
   * @param inputFingerPrint Fingerprint of the input stream
   * @param inputType Type of the input data
   * @param streamID Identifier of the stream
   * @throws MCloudException Error happened
   */
  public void requestInputStream(String inputFingerPrint, MCloudDataType inputType, String streamID) throws MCloudException {
    if (!requestInputStreamImpl(nativeInstancePtr, inputFingerPrint, inputType.stringValue(), streamID)) {
      throw new MCloudException("Error requesting input stream");
    }
  }

  /**
   * Request the display of an output stream.
   * By calling this method the client request the display of the output stream on the display
   * server.
   * For cancelling the request for display, the client needs to disconnect.
   *
   * @throws MCloudException Error happened
   */
  public void requestDisplay() throws MCloudException {
    if (!requestDisplayImpl(nativeInstancePtr)) {
      throw new MCloudException("Error requesting display");
    }
  }
  
  public void setAudioEncoder(MCloudCodec codec, int sampleRate, int  bitRate, int channels) throws MCloudException {
	  if (!setAudioEncoderImpl(nativeInstancePtr, codec.getValue(), sampleRate, bitRate, channels)) {
		throw new MCloudException("Error setting encoder: " + codec.getName());
	  }
  }
  
  public void setAudioEncoder(String codec, int sampleRate, int  bitRate, int channels) throws MCloudException  {
	  if (!setAudioEncoder2Impl(nativeInstancePtr, codec, sampleRate, bitRate, channels)) {
		throw new MCloudException("Error setting encoder: " + codec);
	  }
  }

  /* ************************************************************
   * Native method declarations
   **************************************************************/
  /**
   * Native implementation of addFlowDescription
   *
   * @param nativeInstancePtr Pointer to the native instance of MCloud
   * @param password An optional password which has to be used in order to subscribe to the flow
   *                 (display server), null for no password.
   * @param logging If false the flow will not be logged in the database
   * @param language Descriptive language identifier of the flow, e.g. English
   * @param name Name of the flow, e.g. title of a talk
   * @param description Additional description of the flow, e.g. abstract
   * @return true if success
   */
  private native boolean addFlowDescriptionImpl(long nativeInstancePtr, String password, boolean logging, String language, String name, String description);


  /**
   * Native implementation of announceOutputStream
   *
   * @param nativeInstancePtr Pointer to the native instance of MCloud
   * @param outputFingerPrint Fingerprint of the output stream
   * @param outputType Type of the output data
   * @param streamID Identifier of the stream
   * @param specifier Additional specifier, i.e. a speaker identifier
   * @return true if success
   */
  private native boolean announceOutputStreamImpl(long nativeInstancePtr, String outputFingerPrint, String outputType, String streamID, String specifier);

  /**
   * Native implementation of requestInputStream
   *
   * @param nativeInstancePtr Pointer to the native instance of MCloud
   * @param inputFingerPrint Fingerprint of the input stream
   * @param inputType Type of the input data
   * @param streamID Identifier of the stream
   * @return true if success
   */
  private native boolean requestInputStreamImpl(long nativeInstancePtr, String inputFingerPrint, String inputType, String streamID);

  /**
   * Native implementation of requestDisplay
   *
   * @param nativeInstancePtr Pointer to the native instance of MCloud
   */
  private native boolean requestDisplayImpl(long nativeInstancePtr);
 
 
  /**
   * Native implementation of SetAudioEncoder
   *
	 * @param  mcloudPtr       	reference to an MCloud object
	 * @param  jcodec           MCloud codec used to transmit/receive data to/from the Mediator
	 * @param  jsampleRate      sample rate used to transmit/receive data to/from the Mediator
	 * @param  jbitRate         bit rate used to transmit/receive data to/from the Mediator
	 * @param  jchannels        channels used to transmit/receive data to/from the Mediator
	 * @return                  true if success
   */
  private native boolean setAudioEncoderImpl(long mcloudPtr, int jcodec, int jsampleRate, int  jbitRate, int jchannels);
  
  /**
   * Native implementation of SetAudioEncoder2
   *
	 * @param  mcloudPtr       	reference to an MCloud object
	 * @param  jcodec           codec (string form) used to transmit/receive data to/from the Mediator
	 * @param  jsampleRate      sample rate used to transmit/receive data to/from the Mediator
	 * @param  jbitRate         bit rate used to transmit/receive data to/from the Mediator
	 * @param  jchannels        channels used to transmit/receive data to/from the Mediator
	 * @return                  true if success
   */
  private native boolean setAudioEncoder2Impl(long mcloudPtr, String jcodec, int jsampleRate, int jbitRate, int  jchannels); 
 
}