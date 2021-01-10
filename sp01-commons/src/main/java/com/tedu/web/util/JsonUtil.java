package com.tedu.web.util;

import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import java.io.Writer;
import java.util.Collection;
import java.io.FileWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

public class JsonUtil
{
    private static final Logger log;
    private static ObjectMapper mapper;
    private static JsonInclude.Include DEFAULT_PROPERTY_INCLUSION;
    private static boolean IS_ENABLE_INDENT_OUTPUT;
    private static String CSV_DEFAULT_COLUMN_SEPARATOR;
    
    static {
        log = LoggerFactory.getLogger((Class)JsonUtil.class);
        JsonUtil.DEFAULT_PROPERTY_INCLUSION = JsonInclude.Include.NON_DEFAULT;
        JsonUtil.IS_ENABLE_INDENT_OUTPUT = false;
        JsonUtil.CSV_DEFAULT_COLUMN_SEPARATOR = ",";
        try {
            initMapper();
            configPropertyInclusion();
            configIndentOutput();
            configCommon();
        }
        catch (Exception e) {
            JsonUtil.log.error("jackson config error", (Throwable)e);
        }
    }
    
    private static void initMapper() {
        JsonUtil.mapper = new ObjectMapper();
    }
    
    private static void configCommon() {
        config(JsonUtil.mapper);
    }
    
    private static void configPropertyInclusion() {
        JsonUtil.mapper.setSerializationInclusion(JsonUtil.DEFAULT_PROPERTY_INCLUSION);
    }
    
    private static void configIndentOutput() {
        JsonUtil.mapper.configure(SerializationFeature.INDENT_OUTPUT, JsonUtil.IS_ENABLE_INDENT_OUTPUT);
    }
    
    private static void config(final ObjectMapper objectMapper) {
        objectMapper.enable(new JsonGenerator.Feature[] { JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN });
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(new JsonParser.Feature[] { JsonParser.Feature.ALLOW_COMMENTS });
        objectMapper.disable(new JsonGenerator.Feature[] { JsonGenerator.Feature.ESCAPE_NON_ASCII });
        objectMapper.enable(new JsonGenerator.Feature[] { JsonGenerator.Feature.IGNORE_UNKNOWN });
        objectMapper.enable(new JsonParser.Feature[] { JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES });
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat((DateFormat)new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.enable(new JsonParser.Feature[] { JsonParser.Feature.ALLOW_SINGLE_QUOTES });
        objectMapper.registerModule((Module)new ParameterNamesModule());
        objectMapper.registerModule((Module)new Jdk8Module());
        objectMapper.registerModule((Module)new JavaTimeModule());
        objectMapper.registerModule((Module)new GuavaModule());
    }
    
    public static void setSerializationInclusion(final JsonInclude.Include inclusion) {
        JsonUtil.DEFAULT_PROPERTY_INCLUSION = inclusion;
        configPropertyInclusion();
    }
    
    public static void setIndentOutput(final boolean isEnable) {
        JsonUtil.IS_ENABLE_INDENT_OUTPUT = isEnable;
        configIndentOutput();
    }
    
    public static <V> V from(final URL url, final Class<V> c) {
        try {
            return (V)JsonUtil.mapper.readValue(url, (Class)c);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, url: {}, type: {}", new Object[] { url.getPath(), c, e });
            return null;
        }
    }
    
    public static <V> V from(final InputStream inputStream, final Class<V> c) {
        try {
            return (V)JsonUtil.mapper.readValue(inputStream, (Class)c);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, type: {}", (Object)c, (Object)e);
            return null;
        }
    }
    
    public static <V> V from(final File file, final Class<V> c) {
        try {
            return (V)JsonUtil.mapper.readValue(file, (Class)c);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, file path: {}, type: {}", new Object[] { file.getPath(), c, e });
            return null;
        }
    }
    
    public static <V> V from(final Object jsonObj, final Class<V> c) {
        try {
            return (V)JsonUtil.mapper.readValue(jsonObj.toString(), (Class)c);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, json: {}, type: {}", new Object[] { jsonObj.toString(), c, e });
            return null;
        }
    }
    
    public static <V> V from(final String json, final Class<V> c) {
        try {
            return (V)JsonUtil.mapper.readValue(json, (Class)c);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, json: {}, type: {}", new Object[] { json, c, e });
            return null;
        }
    }
    
    public static <V> V from(final URL url, final TypeReference<V> type) {
        try {
            return (V)JsonUtil.mapper.readValue(url, (TypeReference)type);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, url: {}, type: {}", new Object[] { url.getPath(), type, e });
            return null;
        }
    }
    
    public static <V> V from(final InputStream inputStream, final TypeReference<V> type) {
        try {
            return (V)JsonUtil.mapper.readValue(inputStream, (TypeReference)type);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, type: {}", (Object)type, (Object)e);
            return null;
        }
    }
    
    public static <V> V from(final File file, final TypeReference<V> type) {
        try {
            return (V)JsonUtil.mapper.readValue(file, (TypeReference)type);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, file path: {}, type: {}", new Object[] { file.getPath(), type, e });
            return null;
        }
    }
    
    public static <V> V from(final Object jsonObj, final TypeReference<V> type) {
        try {
            return (V)JsonUtil.mapper.readValue(jsonObj.toString(), (TypeReference)type);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, json: {}, type: {}", new Object[] { jsonObj.toString(), type, e });
            return null;
        }
    }
    
    public static <V> V from(final String json, final TypeReference<V> type) {
        try {
            return (V)JsonUtil.mapper.readValue(json, (TypeReference)type);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson from error, json: {}, type: {}", new Object[] { json, type, e });
            return null;
        }
    }
    
    public static <V> String to(final List<V> list) {
        try {
            return JsonUtil.mapper.writeValueAsString((Object)list);
        }
        catch (JsonProcessingException e) {
            JsonUtil.log.error("jackson to error, obj: {}", (Object)list, (Object)e);
            return null;
        }
    }
    
    public static <V> String to(final V v) {
        try {
            return JsonUtil.mapper.writeValueAsString((Object)v);
        }
        catch (JsonProcessingException e) {
            JsonUtil.log.error("jackson to error, obj: {}", (Object)v, (Object)e);
            return null;
        }
    }
    
    public static <V> void toFile(final String path, final List<V> list) {
        try {
            Throwable t = null;
            try {
                final Writer writer = new FileWriter(new File(path), true);
                try {
                    JsonUtil.mapper.writer().writeValues(writer).writeAll((Collection)list);
                    writer.flush();
                }
                finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2 = null;
                    t = t2;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (Exception e) {
            JsonUtil.log.error("jackson to file error, path: {}, list: {}", new Object[] { path, list, e });
        }
    }
    
    public static <V> void toFile(final String path, final V v) {
        try {
            Throwable t = null;
            try {
                final Writer writer = new FileWriter(new File(path), true);
                try {
                    JsonUtil.mapper.writer().writeValues(writer).write((Object)v);
                    writer.flush();
                }
                finally {
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t2 = null;
                    t = t2;
                }
                else {
                    final Throwable t2 = null;
                    if (t != t2) {
                        t.addSuppressed(t2);
                    }
                }
            }
        }
        catch (Exception e) {
            JsonUtil.log.error("jackson to file error, path: {}, obj: {}", new Object[] { path, v, e });
        }
    }
    
    public static String getString(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            if (node != null) {
                return node.get(key).toString();
            }
            return null;
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson get string error, json: {}, key: {}", new Object[] { json, key, e });
            return null;
        }
    }
    
    public static Integer getInt(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            if (node != null) {
                return node.get(key).intValue();
            }
            return null;
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson get int error, json: {}, key: {}", new Object[] { json, key, e });
            return null;
        }
    }
    
    public static Long getLong(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            if (node != null) {
                return node.get(key).longValue();
            }
            return null;
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson get long error, json: {}, key: {}", new Object[] { json, key, e });
            return null;
        }
    }
    
    public static Double getDouble(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            if (node != null) {
                return node.get(key).doubleValue();
            }
            return null;
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson get double error, json: {}, key: {}", new Object[] { json, key, e });
            return null;
        }
    }
    
    public static BigInteger getBigInteger(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return new BigInteger(String.valueOf(0.0));
        }
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            if (node != null) {
                return node.get(key).bigIntegerValue();
            }
            return null;
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson get biginteger error, json: {}, key: {}", new Object[] { json, key, e });
            return null;
        }
    }
    
    public static BigDecimal getBigDecimal(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            if (node != null) {
                return node.get(key).decimalValue();
            }
            return null;
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson get bigdecimal error, json: {}, key: {}", new Object[] { json, key, e });
            return null;
        }
    }
    
    public static boolean getBoolean(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return false;
        }
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            return node != null && node.get(key).booleanValue();
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson get boolean error, json: {}, key: {}", new Object[] { json, key, e });
            return false;
        }
    }
    
    public static byte[] getByte(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            if (node != null) {
                return node.get(key).binaryValue();
            }
            return null;
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson get byte error, json: {}, key: {}", new Object[] { json, key, e });
            return null;
        }
    }
    
    public static <T> ArrayList<T> getList(final String json, final String key) {
        if (StringUtils.isEmpty((CharSequence)json)) {
            return null;
        }
        final String string = getString(json, key);
        return from(string, (com.fasterxml.jackson.core.type.TypeReference<ArrayList<T>>)new TypeReference<ArrayList<T>>() {});
    }
    
    public static <T> String add(final String json, final String key, final T value) {
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            add(node, key, value);
            return node.toString();
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson add error, json: {}, key: {}, value: {}", new Object[] { json, key, value, e });
            return json;
        }
    }
    
    private static <T> void add(final JsonNode jsonNode, final String key, final T value) {
        if (value instanceof String) {
            ((ObjectNode)jsonNode).put(key, (String)value);
        }
        else if (value instanceof Short) {
            ((ObjectNode)jsonNode).put(key, (Short)value);
        }
        else if (value instanceof Integer) {
            ((ObjectNode)jsonNode).put(key, (Integer)value);
        }
        else if (value instanceof Long) {
            ((ObjectNode)jsonNode).put(key, (Long)value);
        }
        else if (value instanceof Float) {
            ((ObjectNode)jsonNode).put(key, (Float)value);
        }
        else if (value instanceof Double) {
            ((ObjectNode)jsonNode).put(key, (Double)value);
        }
        else if (value instanceof BigDecimal) {
            ((ObjectNode)jsonNode).put(key, (BigDecimal)value);
        }
        else if (value instanceof BigInteger) {
            ((ObjectNode)jsonNode).put(key, (BigInteger)value);
        }
        else if (value instanceof Boolean) {
            ((ObjectNode)jsonNode).put(key, (Boolean)value);
        }
        else if (value instanceof byte[]) {
            ((ObjectNode)jsonNode).put(key, (byte[])(Object)value);
        }
        else {
            ((ObjectNode)jsonNode).put(key, to(value));
        }
    }
    
    public static String remove(final String json, final String key) {
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            ((ObjectNode)node).remove(key);
            return node.toString();
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson remove error, json: {}, key: {}", new Object[] { json, key, e });
            return json;
        }
    }
    
    public static <T> String update(final String json, final String key, final T value) {
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            ((ObjectNode)node).remove(key);
            add(node, key, value);
            return node.toString();
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson update error, json: {}, key: {}, value: {}", new Object[] { json, key, value, e });
            return json;
        }
    }
    
    public static String format(final String json) {
        try {
            final JsonNode node = JsonUtil.mapper.readTree(json);
            return JsonUtil.mapper.writerWithDefaultPrettyPrinter().writeValueAsString((Object)node);
        }
        catch (IOException e) {
            JsonUtil.log.error("jackson format json error, json: {}", (Object)json, (Object)e);
            return json;
        }
    }
    
    public static boolean isJson(final String json) {
        try {
            JsonUtil.mapper.readTree(json);
            return true;
        }
        catch (Exception e) {
            JsonUtil.log.error("jackson check json error, json: {}", (Object)json, (Object)e);
            return false;
        }
    }
    
    private static InputStream getResourceStream(final String name) {
        return JsonUtil.class.getClassLoader().getResourceAsStream(name);
    }
    
    private static InputStreamReader getResourceReader(final InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}
