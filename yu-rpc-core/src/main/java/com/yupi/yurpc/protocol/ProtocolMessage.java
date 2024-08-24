package com.yupi.yurpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private T body;

    @Data
    public static class Header{
        /**
         * 魔数，保证安全性
         */
        private byte magic;

        /**
         * 版本号
         */
        private byte version;

        private byte serializer;

        /**
         * 消息类型： 请求 / 响应
         */
        private byte messageType;

        /**
         * 消息状态： 成功 / 失败 ...
         */
        private byte status;

        private long requestId;

        /**
         * 消息体长度 （单位：字节）
         */
        private int bodyLength;
    }

}
