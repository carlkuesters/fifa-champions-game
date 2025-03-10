package com.carlkuesters.fifachampions;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.*;

public class SceneProcessorAppState extends BaseDisplayAppState {

    private FilterPostProcessor filterPostProcessor;

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        filterPostProcessor = new FilterPostProcessor(application.getAssetManager());
        // Filter post processor should be the last one
        mainApplication.enqueue(() -> addProcessor(filterPostProcessor));
    }

    public void addProcessor(SceneProcessor processor) {
        mainApplication.getViewPort().addProcessor(processor);
    }

    public void addFilter(Filter filter) {
        filterPostProcessor.addFilter(filter);
    }

    public void removeFilter(Filter filter) {
        filterPostProcessor.removeFilter(filter);
    }
}
