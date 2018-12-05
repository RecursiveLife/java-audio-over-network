package melnikov;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.*;
import javax.swing.*;

public class App extends JFrame{

    private Clip clip;
    private Mixer.Info[] mixerInfo;
    private Map<SourceDataLine, Integer> playLine = new HashMap<SourceDataLine, Integer>();
    private Map<TargetDataLine, Integer> recordLine = new HashMap<TargetDataLine, Integer>();
    private AudioFormat format = new AudioFormat(44100, 16, 2, true, false);

    public static void main(String [] args) {
        App app = new App();

    }

    private void initializeAudio() {

        mixerInfo = AudioSystem.getMixerInfo();

        for(int i = 0; i < mixerInfo.length; ++i) {
            //for (Line.Info lineInfo : AudioSystem.getMixer(mixerInfo[i]).getTargetLineInfo()) {
            try {
                TargetDataLine dataLine = AudioSystem.getTargetDataLine(format, mixerInfo[i]);
                dataLine.open();
                System.out.println(dataLine.getFormat());

                //System.out.println("getTDL: " + dataLine);
                dataLine.close();
                recordLine.put(dataLine, i);
            } catch (Exception ex) {
                System.out.println("getTDL: Error: " + ex);
                //ex.printStackTrace();
            }
            //}
            //for (Line.Info lineInfo : AudioSystem.getMixer(mixerInfo[i]).getSourceLineInfo()) {
            try {
                SourceDataLine dataLine = AudioSystem.getSourceDataLine(format, mixerInfo[i]);
                dataLine.open(format);
                //System.out.println("getTDL: " + dataLine);
                dataLine.close();
                playLine.put(dataLine, i);
            } catch (Exception ex) {
                System.out.println("getTDL: Error: " + ex);
            }
            //}
        }
    }

    public App() {
        this.initializeAudio();
        JLabel label1 = new JLabel("SourceDataLine"), label2 = new JLabel("TargetDataLine");
        JComboBox comboBox1 = new JComboBox(), comboBox2 = new JComboBox();
        for(Map.Entry entry : playLine.entrySet()) { comboBox1.addItem("Mixer " + entry.getValue()); }
        for(Map.Entry entry : recordLine.entrySet()) { comboBox2.addItem("Mixer " + entry.getValue()); }
        JButton playButton = new JButton("Play");
        JButton recordButton = new JButton("Record");
        playButton.addActionListener(e -> playTheSound(Integer.parseInt(comboBox1.getSelectedItem().toString().substring(6))));
        //play.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { playTheSound(); }});
        recordButton.addActionListener(e -> recordTheSound(Integer.parseInt(comboBox2.getSelectedItem().toString().substring(6))));
        this.setBounds(500, 200, 220, 150);
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);

        this.add(label1);
        layout.putConstraint(SpringLayout.WEST, label1, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, label1, 5, SpringLayout.NORTH, this);

        this.add(label2);
        layout.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.EAST, label1);
        layout.putConstraint(SpringLayout.NORTH, label2, 5, SpringLayout.NORTH, this);

        this.add(comboBox1);
        layout.putConstraint(SpringLayout.WEST, comboBox1, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, comboBox1, 5, SpringLayout.SOUTH, label1);

        this.add(comboBox2);
        layout.putConstraint(SpringLayout.WEST, comboBox2, 5, SpringLayout.EAST, comboBox1);
        layout.putConstraint(SpringLayout.NORTH, comboBox2, 5, SpringLayout.SOUTH, label1);

        this.add(playButton);
        layout.putConstraint(SpringLayout.WEST, playButton, 5, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, playButton, 5, SpringLayout.SOUTH, comboBox1);

        this.add(recordButton);
        layout.putConstraint(SpringLayout.WEST, recordButton, 5, SpringLayout.EAST, playButton);
        layout.putConstraint(SpringLayout.NORTH, recordButton, 5, SpringLayout.SOUTH, comboBox1);

        //this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void playTheSound(int i) {
        try {
            SourceDataLine dataLine = AudioSystem.getSourceDataLine(format, mixerInfo[i]);
            dataLine.open(format);
            byte tempBuffer[] = new byte[10000];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("D:\\Cat-Meow.wav")));
            dataLine.start();
            while (bis.read(tempBuffer) != -1) {
                dataLine.write(tempBuffer, 0, 10000);
            }
            dataLine.stop();
            dataLine.close();
        } catch (Exception ex) { System.out.println("Error: " + ex); }
    }

    public void recordTheSound(int i) {
        try {
            TargetDataLine dataLine = AudioSystem.getTargetDataLine(format, mixerInfo[i]);
            dataLine.open(format);
            File file = new File("D:\\test.wav");
            if( file.exists() )
                file.delete();
            file.createNewFile();
            dataLine.start();
            AudioInputStream ais = new AudioInputStream(dataLine);
            AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;
            AudioSystem.write(ais, targetType, file);

            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < (long)5000) {
                System.out.println("while");
            }

            dataLine.stop();
            dataLine.close();
        } catch (Exception ex) { System.out.println("Error: " + ex); }
    }
}
