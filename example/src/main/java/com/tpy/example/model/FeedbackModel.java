package com.tpy.example.model;

import com.tpy.pojo.table.TableName;

import java.io.Serializable;

@TableName(name = "feedback")
public class FeedbackModel implements Serializable {
    private static final long serialVersionUID = 4788276224402749400L;
    Long id;
    Long userId;
    String error;
    String remarks;
    String processingTime;
    Long processingUserId;
    Long questionId;

    public Long getId() {
        return id;
    }

    public FeedbackModel setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public FeedbackModel setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getError() {
        return error;
    }

    public FeedbackModel setError(String error) {
        this.error = error;
        return this;
    }

    public String getRemarks() {
        return remarks;
    }

    public FeedbackModel setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public FeedbackModel setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
        return this;
    }

    public Long getProcessingUserId() {
        return processingUserId;
    }

    public FeedbackModel setProcessingUserId(Long processingUserId) {
        this.processingUserId = processingUserId;
        return this;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public FeedbackModel setQuestionId(Long questionId) {
        this.questionId = questionId;
        return this;
    }
}
