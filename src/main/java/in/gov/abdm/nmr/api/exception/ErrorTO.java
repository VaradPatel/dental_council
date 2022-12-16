package in.gov.abdm.nmr.api.exception;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorTO {

	@JsonProperty("timestamp")
	private Date timestamp;

	@JsonProperty("status")
	private Integer status;

	@JsonProperty("error")
	private String error;

	@JsonProperty("path")
	private String path;

	@JsonProperty("message")
	private String message;

	public ErrorTO(Date timestamp, Integer status, String error, String path, String message) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.path = path;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
