package com.code.action;

import com.code.util.BaseUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Classname CreateServiceAction
 * @Description TODO
 * @Date 2020/1/22 17:32
 * @Created by limeng
 */
public class CreateServiceAction extends BaseAnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        this.init(anActionEvent);
        String serviceName = Messages.showInputDialog((String)"Service name", (String)"Create service", (Icon)Messages.getInformationIcon());
        Map<String, String> param = new HashMap<String, String>();
        String newServiceName = BaseUtils.firstLetterUpperCase(BaseUtils.markToHump(serviceName, "_", null));
        param.put("doCalssName", newServiceName);
        param.put("tableName", serviceName);
        final PsiDirectory psiDirectory = this.getPsiDirectory();
        final JavaDirectoryService javaDirectoryService = this.getJavaDirectoryService();
        WriteCommandAction.runWriteCommandAction((Project)this.getProject(), (Runnable)new Runnable(){
            @Override
            public void run() {
                PsiDirectory implDirs = psiDirectory.findSubdirectory("impl");
                if (implDirs == null) {
                    implDirs = psiDirectory.createSubdirectory("impl");
                }
                PsiClass servicePsiClass = javaDirectoryService.createClass(psiDirectory, "", "MMS_Service", false, param);
                String packagePath = servicePsiClass.getQualifiedName();
                String implT = "impl";
                param.put(implT, packagePath + ";");
                param.put("serviceName", newServiceName);
                javaDirectoryService.createClass(implDirs, "", "MMS_ServiceImpl", false, param);
            }
        });
    }
}
