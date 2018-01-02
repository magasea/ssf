package com.shellshellfish.aaas.finance.model.dto;

import java.util.List;

/**
 * @Author pierre
 * 18-1-2
 */
public class FundManager {
	String managerName;
	String workingdays;
	String avgearningrate;
	List<JobDetail> jobDetails;

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getWorkingdays() {
		return workingdays;
	}

	public void setWorkingdays(String workingdays) {
		this.workingdays = workingdays;
	}

	public String getAvgearningrate() {
		return avgearningrate;
	}

	public void setAvgearningrate(String avgearningrate) {
		this.avgearningrate = avgearningrate;
	}

	public List<JobDetail> getJobDetails() {
		return jobDetails;
	}

	public void setJobDetails(List<JobDetail> jobDetails) {
		this.jobDetails = jobDetails;
	}


	@Override
	public String toString() {
		return "FundManager{" +
				"managerName='" + managerName + '\'' +
				", workingdays='" + workingdays + '\'' +
				", avgearningrate='" + avgearningrate + '\'' +
				", jobDetails=" + jobDetails.toString() +
				'}';
	}

	public class JobDetail {
		String fundName;
		String tenure;
		String returns;

		public String getFundName() {
			return fundName;
		}

		public void setFundName(String fundName) {
			this.fundName = fundName;
		}

		public String getTenure() {
			return tenure;
		}

		public void setTenure(String tenure) {
			this.tenure = tenure;
		}

		public String getReturns() {
			return returns;
		}

		public void setReturns(String returns) {
			this.returns = returns;
		}

		public JobDetail(String fundName, String tenure, String returns) {
			this.fundName = fundName;
			this.tenure = tenure;
			this.returns = returns;
		}

		@Override
		public String toString() {
			return "{" +
					"fundName='" + fundName + '\'' +
					", tenure='" + tenure + '\'' +
					", returns='" + returns + '\'' +
					'}';
		}
	}
}
