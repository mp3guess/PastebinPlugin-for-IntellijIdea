package ivantamrazov;

import com.esotericsoftware.kryo.kryo5.minlog.Log;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import groovy.util.logging.Slf4j;

import java.io.IOException;

@Slf4j
public class PastebinAction extends AnAction {

    private final PastebinApiService pastebinApiService;

    public PastebinAction() {
        this.pastebinApiService = new PastebinApiService();
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Log.info("Getting project");
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) {
            Messages.showErrorDialog(project, "No active editor found", "Error");
            return;
        }

        Log.info("Getting selected text");
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedCode = selectionModel.getSelectedText();
        if (selectedCode == null || selectedCode.trim().isEmpty()) {
            selectedCode = editor.getDocument().getText();
        }

        String link;
        try {
            Log.info("Uploading code to pastebin and getting the link");
            link = pastebinApiService.uploadCodeToPastebin(selectedCode);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        Messages.showMessageDialog("Paste was successfully created " + link, "Pastebin Plugin", Messages.getInformationIcon());
    }
}
