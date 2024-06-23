import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Base64;

public class Base64EncoderDecoderGUI extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private JButton encodeButton;
    private JButton decodeButton;

    public Base64EncoderDecoderGUI() {
        createView();

        setTitle("Base64 Encoder/Decoder");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void createView() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        panel.setLayout(new BorderLayout());

        inputField = new JTextField();
        panel.add(inputField, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);

        encodeButton = new JButton("Encode");
        encodeButton.addActionListener(new EncodeButtonListener());
        buttonPanel.add(encodeButton);

        decodeButton = new JButton("Decode");
        decodeButton.addActionListener(new DecodeButtonListener());
        buttonPanel.add(decodeButton);
    }

    private class EncodeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = inputField.getText();
            String encoded = encodeBase64(input);
            outputArea.setText("Encoded: " + encoded);
        }
    }

    private class DecodeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = inputField.getText();
            try {
                String decoded = decodeBase64(input);
                outputArea.setText("Decoded: " + decoded);
            } catch (IllegalArgumentException ex) {
                outputArea.setText("Error: Invalid Base64 input");
            }
        }
    }

    public static String encodeBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String decodeBase64(String input) {
        return new String(Base64.getDecoder().decode(input));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Base64EncoderDecoderGUI().setVisible(true);
            }
        });
    }
}
