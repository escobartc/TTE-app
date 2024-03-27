package com.challenge.tteapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
public class ApprovalJobs {

    private List<JobsResponse> jobsResponseList;
}
