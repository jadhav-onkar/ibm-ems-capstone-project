package com.ibm.attendance.dto;

public class MonthlyReportResponse {

    private String employeeId;
    private int month;
    private int year;

    private long totalWorkingDays;
    private long presentDays;
    private long absentDays;

    private double totalWorkingHours;
    private double overtimeHours;
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public long getTotalWorkingDays() {
		return totalWorkingDays;
	}
	public void setTotalWorkingDays(long totalWorkingDays) {
		this.totalWorkingDays = totalWorkingDays;
	}
	public long getPresentDays() {
		return presentDays;
	}
	public void setPresentDays(long presentDays) {
		this.presentDays = presentDays;
	}
	public long getAbsentDays() {
		return absentDays;
	}
	public void setAbsentDays(long absentDays) {
		this.absentDays = absentDays;
	}
	public double getTotalWorkingHours() {
		return totalWorkingHours;
	}
	public void setTotalWorkingHours(double totalWorkingHours) {
		this.totalWorkingHours = totalWorkingHours;
	}
	public double getOvertimeHours() {
		return overtimeHours;
	}
	public void setOvertimeHours(double overtimeHours) {
		this.overtimeHours = overtimeHours;
	}

    
}
