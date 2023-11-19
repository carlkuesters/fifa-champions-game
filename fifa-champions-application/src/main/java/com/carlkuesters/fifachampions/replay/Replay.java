package com.carlkuesters.fifachampions.replay;

import java.util.ArrayList;

public class Replay {

    private ArrayList<ReplayFrame> frames = new ArrayList<>();

    public void addFrame(ReplayFrame frame, float tpf) {
        frames.add(frame);
    }

    public ReplayFrame getFrame(float replayTime) {
        for (int i = frames.size() - 1; i >= 0; i--) {
            ReplayFrame frame = frames.get(i);
            if (frame.getReplayTime() <= replayTime) {
                if (i == (frames.size() - 1)) {
                    return frame;
                }
                ReplayFrame nextFrame = frames.get(i + 1);
                // TODO: Interpolate to enable proper slow motion
                float distanceToFrame = replayTime - frame.getReplayTime();
                float distanceToNextFrame = nextFrame.getReplayTime() - replayTime;
                return ((distanceToFrame < distanceToNextFrame) ? frame : nextFrame);
            }
        }
        throw new IllegalArgumentException();
    }

    public float getDuration() {
        return frames.get(frames.size() - 1).getReplayTime();
    }
}
