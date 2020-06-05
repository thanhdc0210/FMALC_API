package fmalc.api.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseResponse {

        @JsonIgnore
        private int status;
        @JsonIgnore
        private String message;
        @JsonIgnore
        private Object data;

        public BaseResponse() {
            this.setStatus(ResponseStatusEnum.SUCCESS);
            this.setMessage(ResponseStatusEnum.SUCCESS);
            this.setData(null);
        }

        public int getStatus() {
            return status;
        }
        public void setStatus(ResponseStatusEnum statusEnum) {
            this.status = statusEnum.getValue();
        }
        public String getMessage() {
            return this.message;
        }
        public void setMessage(ResponseStatusEnum statusEnum) {
            switch(statusEnum) {
                case SUCCESS:
                    this.message = "Thành công";
                    break;
                case CREATED:
                    this.message = "Tạo Thành công";
                    break;
                case NO_CONTENT:
                    this.message = "No content";
                    break;
                case PARAMS_INVALID:
                    this.message = "Dữ liệu không hợp lệ";
                    break;
                case NOT_FOUND:
                    this.message = "Not found";
                    break;
                case REQUEST_TIMEOUT:
                    this.message = "Request timeout";
                    break;
                case TOKEN_EXPIRED:
                    this.message = "Token expired";
                    break;
                case UNAUTHORIZED:
                    this.message = "Không có quyền truy cập chức năng này";
                    break;
                case DEVICE_NOT_FOUND:
                    this.message = "Device not found";
                    break;
                case FAIL:
                    this.message = "Internal Server Error";
                    break;
                default:
                    this.message = "404 Not Found";
                    break;
            }
        }

        public void setMessageError(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }
        public void setData(Object data) {
            this.data = data;
        }

    }
