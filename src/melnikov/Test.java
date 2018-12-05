package melnikov;

import javax.sound.sampled.*;

public class Test {
    public static void main(String[] args) {
        TargetDataLine input;
        //SourceDataLine output;
        AudioFormat format = new AudioFormat(44100, 8, 2, true, false);


        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 5; i < mixerInfo.length; ++i) {
            System.out.println("Mixer: " + mixerInfo[i]);
            for (Line.Info lineInfo : AudioSystem.getMixer(mixerInfo[i]).getTargetLineInfo()) {
                try {
                    //input = AudioSystem.getTargetDataLine(format, mixerInfo[i]);
                    Port port = (Port) AudioSystem.getMixer(mixerInfo[i]).getLine(lineInfo);
                    port.open();
                    input = AudioSystem.getTargetDataLine(format, mixerInfo[i]);
                    input.open(format);
                    System.out.println("  Line: " + lineInfo);
                    System.out.println("  Port: " + port);
                    input.close();
                    port.close();
                } catch (Exception ex) {
                    System.out.println("  Port: Error: " + ex);
                    //continue;
                }
                //break;
            }
            System.out.println();
        }
    }
}

/*
long[] sampleRate = {8000,11025,12000,16000,22050,24000,32000,44100,48000,96000,192000};
int[] sampleSizeInBits = {8,16,24,32};
int[] channels = {1,2};
boolean[] signed = {true,false};
boolean[] bigEndian = {true,false};

for (int a = 0; a < sampleRate.length; ++a)
    for (int b = 0; b < sampleSizeInBits.length; ++b)
        for (int c = 0; c < channels.length; ++c)
            for (int d = 0; d < signed.length; ++d)
                for (int e = 0; e < bigEndian.length; ++e) {
                    format = new AudioFormat(sampleRate[a], sampleSizeInBits[b], channels[c], signed[d], bigEndian[e]);
*/
/*
            for(Line.Info lineInfo : AudioSystem.getMixer(mixerInfo).getSourceLineInfo())
            {
                System.out.println("  SourceLine: " + lineInfo);
                if(lineInfo instanceof SourceDataLine.Info)
                {
                    try {
                        output = (SourceDataLine)AudioSystem.getMixer(mixerInfo).getLine(lineInfo);
                        output.open(format); // , 2048
                        System.out.println("    getLine to SDL: " + output);
                        output.close();
                    } catch(Exception ex) {
                        System.out.println("    getLine to SDL: Error: " + ex);
                        //continue;
                    }

                    try {
                        output = AudioSystem.getSourceDataLine(format, mixerInfo);
                        output.open(format); // , 2048
                        System.out.println("    getSDL: " + output);
                        output.close();
                    } catch(Exception ex) {
                        System.out.println("    getSDL: Error: " + ex);
                        //continue;
                    }

                    //break;
                }
            }
*/
