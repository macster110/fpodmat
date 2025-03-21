package org.pamguard.cpodutils;

import org.pamguard.cpodutils.FPODReader.FPODdata;

public class CPODClick {

private double[][] wavData;
	
	private short nCyc;
	private short bw;
	private short kHz;
	private short endF;
	private short spl;
	private short slope;
	private long iciSamples;
	private short[] rawData;
	
	/**
	 * The amplitude in dB. 
	 */
	private Double amplitudedB;
	

	private CPODClassification cpodClassification;

	private double duration;



	private long tMillis;

	private double[] frequency;
	
	
	/**
	 * Make a FPOD click. This called whenever click has been imported from a FP1 or FP3 file
	 * @param tMillis - the time in milliseconds datenum.
	 * @param fileSamples - the number of samples into the file the CPOD click is at - this is calculated from CPODClickDataBlock.CPOD_SR
	 * @param shortData - the raw data from the CPOD click. This can be 8 bytes or 30 bytes if a click train clcik
	 * @return a CPODClick object. 
	 */
	public static CPODClick makeFPODClick(long tMillis, long fileSamples, FPODdata fpodData) {
		
		
		CPODClick cpodClick = new CPODClick(tMillis, fileSamples, (short) fpodData.Ncyc, (short) fpodData.BW, 
				(short) FPODReader.IPItoKhz(fpodData.IPIatMax),  (short) FPODReader.IPItoKhz(fpodData.EndIPI), 
				(short) fpodData.MaxPkExtnd, (short) 0, null);
		
		//DURATION is measured more accurately in FPOD data
		cpodClick.setDurationInMilliseconds((fpodData.duration*5.)/1000.);
		
		//some FPOD clicks have raw wave data - some do not. 
		if (fpodData.getWavData()!=null) {	
			
			int[] waveData = FPODReader.makeResampledWaveform(fpodData);
			
			//now need to scale the data so it fits as a raw data holder. 
			double[] waveDataD = FPODReader.scaleWavData(waveData);
			
			cpodClick.wavData = new double[1][];//create a 2D array
			cpodClick.wavData[0]=waveDataD;
			
		}
		
		return cpodClick;
	}
	

	/**
	 * Make a CPOD click. This called whenever click has been imported from a CP1 or CP3 file
	 * @param tMillis - the time in milliseconds datenum.
	 * @param fileSamples - the number of samples into the file the CPOD click is at - this is calculated from CPODClickDataBlock.CPOD_SR
	 * @param shortData - the raw data from the CPOD click. This can be 8 bytes or 40 bytes if a click train clcik
	 * @return a CPODClick object. 
	 */
	public static CPODClick makeCPODClick(long tMillis, long fileSamples, short[] shortData) {
		
		short nCyc = shortData[3];
		short bw = shortData[4]; //bandwidth is an arbitary scale between 0 and 31; 
		bw = (short) ((255./31.) * (bw+1)); //make some attempt to convert to kHz
		short kHz = shortData[5];
		short endF = shortData[6];
		short spl = shortData[7];
		short slope = shortData[8];
		CPODClick cpodClick = new CPODClick(tMillis, fileSamples, nCyc, bw, kHz, endF, spl, slope, shortData);
		
//		//estimate the duration in millis - not accurate but gives an idea.
		double duration = (nCyc/(double) kHz);
		cpodClick.setDurationInMilliseconds(duration);
		
		
		return cpodClick;
	}

	private void setDurationInMilliseconds(double duration) {
		this.duration= duration;
	}


	/**
	 * Constructor for a CPOD click. This adds all basic information that is required for a CPOD or FPOD click
	 * @param tMillis - the time in millis. 
	 * @param fileSamples - the file samples
	 * @param nCyc - the number of cycles
	 * @param bw -the bandwidth in kHZ
	 * @param kHz - the frequency in kHz. 
	 * @param endF - the end frequency in kHz. 
	 * @param spl - the spl (0-255) unitless.
	 * @param slope - the slope
	 * @param shortData - the raw data. 
	 */
	public CPODClick(long tMillis, long fileSamples, short nCyc, short bw,
			short kHz, short endF, short spl, short slope, short[] shortData) {
		
		this.tMillis = tMillis;
		this.nCyc = nCyc;
		this.bw = bw;
		this.kHz = kHz;
		this.endF = endF;
		this.spl = spl;
		this.slope = slope;
		double[] f = new double[2];
		
		f[0] = (kHz - bw/2.)*1000.;
		f[1] = (kHz + bw/2.)*1000.;
		
		setFrequency(f);
		
//		double duration = (nCyc/(double) kHz);
//		setDurationInMilliseconds(duration);
	
		if (shortData!=null) {
			//only CPOD
			this.rawData = shortData.clone();
		}
	}

	private void setFrequency(double[] f) {
		this.frequency = f;
	}

	/**
	 * Get the duration in milliseconds. 
	 * @return the duration in millis
	 */
	public double getDuration() {
		return duration;
	}

	/**
	 * Get the frequency limits in Hz
	 * @return the frequency limits in Hertz
	 */
	public double[] getFrequency() {
		return frequency;
	}

	/**
	 * Get the raw data. 
	 * @return the raw data. 
	 */
	public short[] getRawData() {
		return rawData;
	}

	/**
	 * @return the nCyc
	 */
	public short getnCyc() {
		return nCyc;
	}

	/**
	 * @param nCyc the nCyc to set
	 */
	public void setnCyc(short nCyc) {
		this.nCyc = nCyc;
	}

	/**
	 * 
	 * Get the bandwidth in kHz
	 * @return the bw
	 */
	public short getBw() {
		return bw;
	}

	/**
	 * @param bw the bw to set
	 */
	public void setBw(short bw) {
		this.bw = bw;
	}

	/**
	 * @return the kHz
	 */
	public short getkHz() {
		return kHz;
	}

	/**
	 * @param kHz the kHz to set
	 */
	public void setkHz(short kHz) {
		this.kHz = kHz;
	}

	/**
	 * @return the endF
	 */
	public short getEndF() {
		return endF;
	}

	/**
	 * @param endF the endF to set
	 */
	public void setEndF(short endF) {
		this.endF = endF;
	}

	/**
	 * @return the spl
	 */
	public short getSpl() {
		return spl;
	}

	/**
	 * @param spl the spl to set
	 */
	public void setSpl(short spl) {
		this.spl = spl;
	}

	/**
	 * @return the slope
	 */
	public short getSlope() {
		return slope;
	}

	/**
	 * @param slope the slope to set
	 */
	public void setSlope(short slope) {
		this.slope = slope;
	}

	public void setICISamples(long iciSamples) {
		this.iciSamples = iciSamples;
	}
	
	public long getICISamples() {
		return iciSamples;
	}
	
	/**
	 * Get a rough estimation of the recieved amplitude of a CPOD in dB
	 * @return the amplitude in dB. 
	 */
	public double getAmplitudeDB() {
		if (amplitudedB==null) {
			amplitudedB = 20*Math.log10(spl) +90;
		}
		return amplitudedB;
	}
	

	/* (non-Javadoc)
	 * @see PamDetection.AcousticDataUnit#getSummaryString()
	 */
	public String getSummaryString() {
		//System.out.println("Hello CPOD summary string:"); 
		
		String str = "<html>";
		long tm = getTimeMilliseconds();
		str += CPODUtils.formatDateTime(tm) + "<p>";
		str += String.format("Start Freq: %dkHz<p>", getkHz());
		str += String.format("N Cycles: %d<p>", getnCyc());
		str += String.format("BandWidth: %dkHz<p>", getBw());
		str += String.format("End Freq: %dkHz<p>", getEndF());
		str += String.format("Slope: %d<p>", getSlope());
		str += String.format("SPL: %d", getSpl());
		if (rawData != null && rawData.length == 40) {
			str += String.format("<p>QClass %d, SpClass %d", CPODUtils.getBits(rawData[19], (short) 0x3), 
					CPODUtils.getBits(rawData[19], (short) 0b11100));
			str += String.format("<p>Train %d, %d click", rawData[20], rawData[23]);
			str += String.format("<p>Qn %d, RateGood %d, SpGood %d, SpClass %d",
					CPODUtils.getBits(rawData[36], (short)3), CPODUtils.getBits(rawData[36], (short) 4),
					CPODUtils.getBits(rawData[36], (short)8), CPODUtils.getBits(rawData[36], (short) 240));
		}
		if (rawData != null) {
			int nRaw = rawData.length;
			int nRow = nRaw/5;
			for (int r = 0; r < nRow; r++) {
				str += "<p>Raw: ";
				for (int i = 0; i < 5; i++) {
					str+= String.format("%03d, ", rawData[r*5+i]);
				}
			}
		}
		
//		str += "<\html>";
		return str;
	}


	
	public long getTimeMilliseconds() {
		return tMillis;
	}


	public double[][] getWaveData() {
		return this.wavData;
	}

	public void setWavData(double[][] ds) {
		this.wavData=ds;
	}


	public void setClassification(CPODClassification cpodClassification) {
		this.cpodClassification = cpodClassification;
	}

	
	public CPODClassification getClassification() {
		return this.cpodClassification;
	}


}

