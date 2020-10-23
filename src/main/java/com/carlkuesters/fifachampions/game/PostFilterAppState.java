/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carlkuesters.fifachampions.game;

import java.util.LinkedList;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.post.*;

/**
 *
 * @author Carl
 */
public class PostFilterAppState extends BaseDisplayAppState {

    public PostFilterAppState(){
        
    }
    private FilterPostProcessor filterPostProcessor;
    private LinkedList<Filter> queuedFilters = new LinkedList<>();

    @Override
    public void initialize(AppStateManager stateManager, Application application){
        super.initialize(stateManager, application);
        filterPostProcessor = new FilterPostProcessor(application.getAssetManager());
        for(Filter filter : queuedFilters){
            filterPostProcessor.addFilter(filter);
        }
        setEnabled(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            mainApplication.getViewPort().addProcessor(filterPostProcessor);
        } else{
            mainApplication.getViewPort().removeProcessor(filterPostProcessor);
        }
    }

    @Override
    public void cleanup(){
        super.cleanup();
        mainApplication.getViewPort().removeProcessor(filterPostProcessor);
    }
    
    public void addFilter(Filter filter) {
        if(isInitialized()){
            mainApplication.enqueueTask(() -> {
                filterPostProcessor.addFilter(filter);
            });
        } else {
            queuedFilters.add(filter);
        }
    }
    
    public void removeFilter(Filter filter) {
        if (isInitialized()) {
            mainApplication.enqueueTask(() -> {
                filterPostProcessor.removeFilter(filter);
            });
        } else {
            queuedFilters.remove(filter);
        }
    }
}
