package it.unibo.exam.view.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;

public class OptionsDialog extends JDialog {
    private static final Preferences PREFS = Preferences.userNodeForPackage(OptionsDialog.class);

    private boolean soundOn = true;
    private int selectedFPS = 60;

    public OptionsDialog(JFrame parent) {
        super(parent, "Opzioni", true);

        // --- CARICA impostazioni salvate ---
        soundOn = PREFS.getBoolean("soundOn", true);
        selectedFPS = PREFS.getInt("fps", 60);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Audio ON/OFF
        JCheckBox soundCheck = new JCheckBox("Abilita suoni", soundOn);
        gbc.gridx = 0; gbc.gridy = 0;
        add(soundCheck, gbc);

        // FPS selector
        gbc.gridy++;
        add(new JLabel("Limite FPS:"), gbc);
        JComboBox<String> fpsCombo = new JComboBox<>(new String[] { "30", "60" });
        fpsCombo.setSelectedItem(String.valueOf(selectedFPS));
        gbc.gridx = 1;
        add(fpsCombo, gbc);

        // Mostra controlli
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        JTextArea controlsArea = new JTextArea(
            "Comandi principali:\n"
            + "- Movimento: W / A / S / D\n"
            + "- Interagisci con NPC/Porte: E\n"
            + "- Menu: ESC"
        );
        controlsArea.setEditable(false);
        controlsArea.setFont(new Font("Arial", Font.PLAIN, 16));
        controlsArea.setBackground(getBackground());
        controlsArea.setFocusable(false);
        controlsArea.setBorder(null);
        add(controlsArea, gbc);

        // Bottoni
        JPanel buttonPanel = new JPanel();
        JButton ok = new JButton("Salva");
        JButton cancel = new JButton("Annulla");
        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        ok.addActionListener(e -> {
            soundOn = soundCheck.isSelected();
            selectedFPS = Integer.parseInt((String) fpsCombo.getSelectedItem());
            PREFS.putBoolean("soundOn", soundOn);
            PREFS.putInt("fps", selectedFPS);
            dispose();
        });
        cancel.addActionListener((ActionEvent e) -> dispose());

        setSize(350, 300);
        setLocationRelativeTo(parent);
    }

    // Getter se vuoi accedere alle opzioni scelte
    public boolean isSoundOn() { return soundOn; }
    public int getSelectedFPS() { return selectedFPS; }
}
