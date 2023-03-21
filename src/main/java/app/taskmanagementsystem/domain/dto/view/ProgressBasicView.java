package app.taskmanagementsystem.domain.dto.view;

import app.taskmanagementsystem.domain.entity.enums.ProgressTypeEnum;

public class ProgressBasicView {
    private Long id;
    private ProgressTypeEnum progress;

    public ProgressBasicView() {
    }

    public Long getId() {
        return id;
    }

    public ProgressBasicView setId(Long id) {
        this.id = id;
        return this;
    }

    public ProgressTypeEnum getProgress() {
        return progress;
    }

    public ProgressBasicView setProgress(ProgressTypeEnum progress) {
        this.progress = progress;
        return this;
    }
}
