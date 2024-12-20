%test the import function. 

%FPOD file
fpodfile = "/Users/jdjm/Library/CloudStorage/Dropbox/PAMGuard_dev/CPOD/FPOD_NunBank/0866 NunBankB 2023 06 27 FPOD_6480 file0.FP3";
%CPOD file
cpodfile = "/Users/jdjm/Library/CloudStorage/Dropbox/PAMGuard_dev/tutorials/CPOD_wav/data/Hyskeir/CPOD/0740 Hyskeir 2022 12 02 POD1655 file01.CP3";

%import the data
[fpodat] = importpoddata(fpodfile, 'detstart', 0, 'maxdet', 100000);
disp(['Imported ' num2str(length(fpodat)) ' FPOD clicks']);

% %import the data
% [cpodat] = importpoddata(cpodfile);
% disp(['Imported ' num2str(length(cpodat)) ' CPOD clicks']); 


ampdB = [fpodat.ampdB];
frequency = [fpodat.freqcenter];
date = [fpodat.date]; 

c = frequency/1000; 

scatter(date, ampdB, 10, frequency, 'filled'); 
ylabel('Amplitude (dB)'); 
set(gca, 'FontSize', 14)

