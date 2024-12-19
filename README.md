# FPOD and CPOD functions in Java and MATLAB

## Introduction
CPODs and their successor, FPODs are underwater click loggers which can detect echolocation clicks from dolphins and porpoise and other transient sounds. The devices do not record sound, but instead record the time, frequency and host of other metrics for each detected transient sound. This greatly decreases the data volume that devices store during a typically deployment. CPOD and FPODs store their data in bespoke CP1 and FP1 files respectively. These are then processed to detect typical sequences of clicks which are indicative of a porpoise or dolphin. The resulting clicks _trains_ are stored in CP3 and FP3 files. 

## MATLAB CPOD FPOD library
The _fpodmat_ library allow users to import CP1/CP3 and FP1/FP3 files. The library calls compiled java code to import the files. The code is based on the importer in [PAMGuard](https://www.pamguard.org/) which is much faster than a native MATLAB implementation. 

Typical usage to import a single file.

To import a single file

```Matlab
%FPOD file
fpodfile = "/path/to/my/ffile/0866 NunBankB 2023 06 27 FPOD_6480 file0.FP3";

%import the data
[fpodat] = importpoddata(fpodfile);
disp(['Imported ' num2str(length(fpodat)) ' FPOD clicks']);

```
```fpodat``` is a an array of struct with each element one click. The struct fields are described in detail in ```importpoddata.m```

You can use the struct to data for downstream analysis fo POD dat. For example plot some data.

```Matlab
ampdB = [fpodat.ampdB];
frequency = [fpodat.freqcenter];
date = [fpodat.date]; 

c = frequency/1000; 

scatter(date, ampdB, 10, frequency, 'filled'); 
ylabel('Amplitude (dB)'); 
set(gca, 'FontSize', 14)
```
![FPOD image](/resources/example_plot_fpod.png)

Note that we are essentially converting CPOD data from a very efficient file format to very inefficient one (MATLAB struct). This means that imported data can be take up a lot of memory. If this becomes an issue then the import function can import only sections of data. For example

```
[fpodat] = importpoddata(fpodfile, 'detstart', 0, 'maxdet', 100000);
```

imports the first 100,000 clicks. This function could be used to extract the required information from the struct and pass into a standard array which is much more memory efficient. 
