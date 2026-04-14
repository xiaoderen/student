    package com.example.attendancesystem.util;

    /**
     * 全局统一返回结果类
     */
    public class Result<T> {
        // 状态码：200成功，500失败
        private Integer code;
        // 提示信息
        private String message;
        // 返回数据
        private T data;

        // 无参构造
        public Result() {}

        // 全参构造
        public Result(Integer code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        // 静态成功方法（带数据）
        public static <T> Result<T> success(T data) {
            return new Result<>(200, "操作成功", data);
        }

        // 静态成功方法（不带数据）
        public static <T> Result<T> success() {
            return new Result<>(200, "操作成功", null);
        }

        // 静态失败方法（带错误信息）
        public static <T> Result<T> error(String message) {
            return new Result<>(500, message, null);
        }

        // Getters and Setters
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }