package me.imlukas.jobsplugin.utils.schedulerutil.builders;

import lombok.Getter;
import me.imlukas.jobsplugin.utils.schedulerutil.data.ScheduleBuilderBase;
import me.imlukas.jobsplugin.utils.schedulerutil.data.ScheduleData;

public class RepeatableT2 implements ScheduleBuilderBase {

    @Getter
    private final ScheduleData data;

    RepeatableT2(ScheduleData data) {
        this.data = data;
    }

    public RepeatableBuilder run(Runnable runnable) {
        data.setRunnable(runnable);
        return new RepeatableBuilder(data);
    }
}
