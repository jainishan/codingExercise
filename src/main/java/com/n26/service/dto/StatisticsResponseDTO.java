package com.n26.service.dto;

public class StatisticsResponseDTO {

	private Double sum = (double) 0;
	
	private Double avg = (double) 0;
	
	private Double max = null;
	
	private Double min = null;
	
	private Long count = 0L;

	
	public void updateStatisticsForAmount(Double amount) {
		
		this.count++;
		
		if (this.max == null) {
			
			this.max = amount;
		} else {
			
			this.setMax(Math.max(max, amount));
		}
		
		if (this.min == null) {
			
			this.min = amount;
		} else {
			
			this.setMin(Math.min(min, amount));
		}
		
		this.setSum(this.sum + amount);
		this.setAvg(this.sum / count);
	}


	public Double getSum() {
		return sum;
	}


	public void setSum(Double sum) {
		this.sum = sum;
	}


	public Double getAvg() {
		return avg;
	}


	public void setAvg(Double avg) {
		this.avg = avg;
	}


	public Double getMax() {
		return max;
	}


	public void setMax(Double max) {
		this.max = max;
	}


	public Double getMin() {
		return min;
	}


	public void setMin(Double min) {
		this.min = min;
	}


	public Long getCount() {
		return count;
	}


	public void setCount(Long count) {
		this.count = count;
	}
	
}
