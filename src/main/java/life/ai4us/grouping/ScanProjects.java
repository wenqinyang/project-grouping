package life.ai4us.grouping;


import com.intellij.ide.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wenqin
 */
public class ScanProjects implements AppLifecycleListener {

    @Override
    public void welcomeScreenDisplayed() {
        RecentProjectsManagerBase instanceEx = RecentProjectsManagerBase.getInstanceEx();
        RecentProjectManagerState state = instanceEx.getState();
        Map<String, RecentProjectMetaInfo> additionalInfo = state.getAdditionalInfo();
        List<String> projectPathList = new ArrayList<>();
        additionalInfo.forEach((key, value) -> projectPathList.add(key));
        Map<String, List<String>> projectMap = projectPathHandler(projectPathList);
        autoAddGroup(projectMap, instanceEx);

    }

    private void autoAddGroup(Map<String, List<String>> projectMap, RecentProjectsManagerBase instanceEx) {
        projectMap.forEach(
                (key, value) -> {
                    ProjectGroup projectGroup  = new ProjectGroup();
                    projectGroup.setName(key);
                    projectGroup.setProjects(value);
                    instanceEx.addGroup(projectGroup);
                }
        );

    }

    private Map<String, List<String>> projectPathHandler(List<String> projectPathList) {
        Map<String, List<String>> groupedPaths = new HashMap<>();
        for (String path : projectPathList) {
            String parentDir = Paths.get(path).getParent().getFileName().toString();
            groupedPaths.putIfAbsent(parentDir, new ArrayList<>());
            groupedPaths.get(parentDir).add(path);
        }
        return groupedPaths;
    }
}
