package com.todoroo.astrid.actfm.sync.messages;

import com.todoroo.andlib.data.Property;
import com.todoroo.astrid.data.TagData;
import com.todoroo.astrid.data.Task;
import com.todoroo.astrid.data.TaskAttachment;
import com.todoroo.astrid.data.TaskListMetadata;
import com.todoroo.astrid.data.User;
import com.todoroo.astrid.data.UserActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NameMaps {

    // --------------------------------
    // ---- Table name mappings -------
    // --------------------------------

    // Universal table identifiers
    public static final String TABLE_ID_TASKS = "tasks";
    public static final String TABLE_ID_TAGS = "tags";
    public static final String TABLE_ID_USERS = "users";
    public static final String TABLE_ID_USER_ACTIVITY = "user_activities";
    public static final String TABLE_ID_HISTORY = "history";
    public static final String TABLE_ID_ATTACHMENTS = "task_attachments";
    public static final String TABLE_ID_TASK_LIST_METADATA = "task_list_metadata";

    // --------------------------------
    // ---- Column name mappings -------
    // --------------------------------
    private static void putPropertyToServerName(Property<?> property, String serverName,
            Map<Property<?>, String> propertyMap, Map<String, Property<?>> localNameMap, Map<String, String> serverNameMap,
            Set<String> excludedFromOutstandingSet, boolean writeable) {
        propertyMap.put(property, serverName);
        localNameMap.put(property.name, property);
        serverNameMap.put(property.name, serverName);
        if (!writeable && excludedFromOutstandingSet != null) {
            excludedFromOutstandingSet.add(property.name);
        }
    }

    // ----------
    // Tasks
    // ----------

    private static final Map<Property<?>, String> TASK_PROPERTIES_LOCAL_TO_SERVER;
    private static final Map<String, Property<?>> TASK_COLUMN_NAMES_TO_PROPERTIES;
    private static final Map<String, String> TASK_COLUMNS_LOCAL_TO_SERVER;
    private static final Set<String> TASK_PROPERTIES_EXCLUDED;


    private static void putTaskPropertyToServerName(Property<?> property, String serverName, boolean writeable) {
        putPropertyToServerName(property, serverName, TASK_PROPERTIES_LOCAL_TO_SERVER, TASK_COLUMN_NAMES_TO_PROPERTIES,
                TASK_COLUMNS_LOCAL_TO_SERVER, TASK_PROPERTIES_EXCLUDED, writeable);
    }

    static {
        // Hardcoded local columns mapped to corresponding server names
        TASK_PROPERTIES_LOCAL_TO_SERVER = new HashMap<Property<?>, String>();
        TASK_COLUMNS_LOCAL_TO_SERVER = new HashMap<String, String>();
        TASK_COLUMN_NAMES_TO_PROPERTIES = new HashMap<String, Property<?>>();
        TASK_PROPERTIES_EXCLUDED = new HashSet<String>();

        putTaskPropertyToServerName(Task.TITLE,           "title",          true);
        putTaskPropertyToServerName(Task.IMPORTANCE,      "importance",     true);
        putTaskPropertyToServerName(Task.DUE_DATE,        "due",            true);
        putTaskPropertyToServerName(Task.HIDE_UNTIL,      "hide_until",     true);
        putTaskPropertyToServerName(Task.CREATION_DATE,   "created_at",     true);
        putTaskPropertyToServerName(Task.COMPLETION_DATE, "completed_at",   true);
        putTaskPropertyToServerName(Task.RECURRENCE,      "repeat",         true);
        putTaskPropertyToServerName(Task.DELETION_DATE,   "deleted_at",     true);
        putTaskPropertyToServerName(Task.NOTES,           "notes",          true);
        putTaskPropertyToServerName(Task.RECURRENCE,      "repeat",         true);
        putTaskPropertyToServerName(Task.USER_ID,         "user_id",        true);
        putTaskPropertyToServerName(Task.CREATOR_ID,      "creator_id",     false);
        putTaskPropertyToServerName(Task.UUID,            "uuid",           false);
        putTaskPropertyToServerName(Task.IS_PUBLIC,       "public",         true);
        putTaskPropertyToServerName(Task.IS_READONLY,     "read_only",      false);
        putTaskPropertyToServerName(Task.CLASSIFICATION,  "classification", false);
    }

    public static final String TAG_ADDED_COLUMN = "tag_added";
    public static final String TAG_REMOVED_COLUMN = "tag_removed";


    // ----------
    // TagData
    // ----------

    private static final Map<Property<?>, String> TAG_DATA_PROPERTIES_LOCAL_TO_SERVER;
    private static final Map<String, Property<?>> TAG_DATA_COLUMN_NAMES_TO_PROPERTIES;
    private static final Map<String, String> TAG_DATA_COLUMNS_LOCAL_TO_SERVER;
    private static final Set<String> TAG_PROPERTIES_EXCLUDED;

    private static void putTagPropertyToServerName(Property<?> property, String serverName, boolean writeable) {
        putPropertyToServerName(property, serverName, TAG_DATA_PROPERTIES_LOCAL_TO_SERVER, TAG_DATA_COLUMN_NAMES_TO_PROPERTIES,
                TAG_DATA_COLUMNS_LOCAL_TO_SERVER, TAG_PROPERTIES_EXCLUDED, writeable);
    }
    static {
        // Hardcoded local columns mapped to corresponding server names
        TAG_DATA_PROPERTIES_LOCAL_TO_SERVER = new HashMap<Property<?>, String>();
        TAG_DATA_COLUMNS_LOCAL_TO_SERVER = new HashMap<String, String>();
        TAG_DATA_COLUMN_NAMES_TO_PROPERTIES = new HashMap<String, Property<?>>();
        TAG_PROPERTIES_EXCLUDED = new HashSet<String>();

        putTagPropertyToServerName(TagData.USER_ID,         "user_id",      true);
        putTagPropertyToServerName(TagData.NAME,            "name",         true);
        putTagPropertyToServerName(TagData.CREATION_DATE,   "created_at",   true);
        putTagPropertyToServerName(TagData.DELETION_DATE,   "deleted_at",   true);
        putTagPropertyToServerName(TagData.UUID,            "uuid",         false);
        putTagPropertyToServerName(TagData.TASK_COUNT,      "task_count",   false);
        putTagPropertyToServerName(TagData.TAG_DESCRIPTION, "description",  true);
        putTagPropertyToServerName(TagData.PICTURE,         "picture",      true);
        putTagPropertyToServerName(TagData.IS_FOLDER,       "is_folder",    false);
    }

    public static final String MEMBER_ADDED_COLUMN = "member_added";
    public static final String MEMBER_REMOVED_COLUMN = "member_removed";

    // ----------
    // Users
    // ----------
    private static final Map<Property<?>, String> USER_PROPERTIES_LOCAL_TO_SERVER;
    private static final Map<String, Property<?>> USER_COLUMN_NAMES_TO_PROPERTIES;
    private static final Map<String, String> USER_COLUMNS_LOCAL_TO_SERVER;
    private static final Set<String> USER_PROPERTIES_EXCLUDED;

    private static void putUserPropertyToServerName(Property<?> property, String serverName, boolean writeable) {
        putPropertyToServerName(property, serverName, USER_PROPERTIES_LOCAL_TO_SERVER, USER_COLUMN_NAMES_TO_PROPERTIES,
                USER_COLUMNS_LOCAL_TO_SERVER, USER_PROPERTIES_EXCLUDED, writeable);
    }

    static {
        USER_PROPERTIES_LOCAL_TO_SERVER = new HashMap<Property<?>, String>();
        USER_COLUMN_NAMES_TO_PROPERTIES = new HashMap<String, Property<?>>();
        USER_COLUMNS_LOCAL_TO_SERVER = new HashMap<String, String>();
        USER_PROPERTIES_EXCLUDED = new HashSet<String>();

        putUserPropertyToServerName(User.UUID,       "uuid",       false);
        putUserPropertyToServerName(User.PICTURE,    "picture",    false);
        putUserPropertyToServerName(User.FIRST_NAME, "first_name", false);
        putUserPropertyToServerName(User.LAST_NAME,  "last_name",  false);
        putUserPropertyToServerName(User.STATUS,     "connection", true);
    }

    // ----------
    // User Activity
    // ----------
    private static final Map<Property<?>, String> USER_ACTIVITY_PROPERTIES_LOCAL_TO_SERVER;
    private static final Map<String, Property<?>> USER_ACTIVITY_COLUMN_NAMES_TO_PROPERTIES;
    private static final Map<String, String> USER_ACTIVITY_COLUMNS_LOCAL_TO_SERVER;
    private static final Set<String> USER_ACTIVITY_PROPERTIES_EXCLUDED;

    private static void putUserActivityPropertyToServerName(Property<?> property, String serverName, boolean writeable) {
        putPropertyToServerName(property, serverName, USER_ACTIVITY_PROPERTIES_LOCAL_TO_SERVER, USER_ACTIVITY_COLUMN_NAMES_TO_PROPERTIES,
                USER_ACTIVITY_COLUMNS_LOCAL_TO_SERVER, USER_ACTIVITY_PROPERTIES_EXCLUDED, writeable);
    }

    static {
        USER_ACTIVITY_PROPERTIES_LOCAL_TO_SERVER = new HashMap<Property<?>, String>();
        USER_ACTIVITY_COLUMN_NAMES_TO_PROPERTIES = new HashMap<String, Property<?>>();
        USER_ACTIVITY_COLUMNS_LOCAL_TO_SERVER = new HashMap<String, String>();
        USER_ACTIVITY_PROPERTIES_EXCLUDED = new HashSet<String>();

        putUserActivityPropertyToServerName(UserActivity.UUID,        "uuid",        false);
        putUserActivityPropertyToServerName(UserActivity.USER_UUID,   "user_id",     false);
        putUserActivityPropertyToServerName(UserActivity.ACTION,      "action",      true);
        putUserActivityPropertyToServerName(UserActivity.MESSAGE,     "message",     true);
        putUserActivityPropertyToServerName(UserActivity.PICTURE,     "picture",     true);
        putUserActivityPropertyToServerName(UserActivity.TARGET_ID,   "target_id",   true);
        putUserActivityPropertyToServerName(UserActivity.TARGET_NAME, "target_name", false);
        putUserActivityPropertyToServerName(UserActivity.CREATED_AT,  "created_at",  true);
        putUserActivityPropertyToServerName(UserActivity.DELETED_AT,  "deleted_at",  true);
    }

    // ----------
    // TaskAttachment
    // ----------
    private static final Map<Property<?>, String> TASK_ATTACHMENT_PROPERTIES_LOCAL_TO_SERVER;
    private static final Map<String, Property<?>> TASK_ATTACHMENT_COLUMN_NAMES_TO_PROPERTIES;
    private static final Map<String, String> TASK_ATTACHMENT_COLUMNS_LOCAL_TO_SERVER;
    private static final Set<String> TASK_ATTACHMENT_PROPERTIES_EXCLUDED;

    public static final String ATTACHMENT_ADDED_COLUMN = "file";

    private static void putTaskAttachmentPropertyToServerName(Property<?> property, String serverName, boolean writeable) {
        putPropertyToServerName(property, serverName, TASK_ATTACHMENT_PROPERTIES_LOCAL_TO_SERVER, TASK_ATTACHMENT_COLUMN_NAMES_TO_PROPERTIES,
                TASK_ATTACHMENT_COLUMNS_LOCAL_TO_SERVER, TASK_ATTACHMENT_PROPERTIES_EXCLUDED, writeable);
    }

    static {
        TASK_ATTACHMENT_PROPERTIES_LOCAL_TO_SERVER = new HashMap<Property<?>, String>();
        TASK_ATTACHMENT_COLUMN_NAMES_TO_PROPERTIES = new HashMap<String, Property<?>>();
        TASK_ATTACHMENT_COLUMNS_LOCAL_TO_SERVER = new HashMap<String, String>();
        TASK_ATTACHMENT_PROPERTIES_EXCLUDED = new HashSet<String>();

        putTaskAttachmentPropertyToServerName(TaskAttachment.UUID,         "uuid",         false);
        putTaskAttachmentPropertyToServerName(TaskAttachment.USER_UUID,    "user_id",      false);
        putTaskAttachmentPropertyToServerName(TaskAttachment.TASK_UUID,    "task_id",      true);
        putTaskAttachmentPropertyToServerName(TaskAttachment.NAME,         "name",         false);
        putTaskAttachmentPropertyToServerName(TaskAttachment.URL,          "url",          false);
        putTaskAttachmentPropertyToServerName(TaskAttachment.SIZE,         "size",         false);
        putTaskAttachmentPropertyToServerName(TaskAttachment.CONTENT_TYPE, "content_type", false);
        putTaskAttachmentPropertyToServerName(TaskAttachment.CREATED_AT,   "created_at",   true);
        putTaskAttachmentPropertyToServerName(TaskAttachment.DELETED_AT,   "deleted_at",   true);
    }

    // ----------
    // TaskListMetadata
    // ----------
    private static final Map<Property<?>, String> TASK_LIST_METADATA_PROPERTIES_LOCAL_TO_SERVER;
    private static final Map<String, Property<?>> TASK_LIST_METADATA_COLUMN_NAMES_TO_PROPERTIES;
    private static final Map<String, String> TASK_LIST_METADATA_COLUMNS_LOCAL_TO_SERVER;
    private static final Set<String> TASK_LIST_METADATA_PROPERTIES_EXCLUDED;

    private static void putTaskListMetadataPropertyToServerName(Property<?> property, String serverName, boolean writeable) {
        putPropertyToServerName(property, serverName, TASK_LIST_METADATA_PROPERTIES_LOCAL_TO_SERVER, TASK_LIST_METADATA_COLUMN_NAMES_TO_PROPERTIES,
                TASK_LIST_METADATA_COLUMNS_LOCAL_TO_SERVER, TASK_LIST_METADATA_PROPERTIES_EXCLUDED, writeable);
    }

    static {
        TASK_LIST_METADATA_PROPERTIES_LOCAL_TO_SERVER = new HashMap<Property<?>, String>();
        TASK_LIST_METADATA_COLUMN_NAMES_TO_PROPERTIES = new HashMap<String, Property<?>>();
        TASK_LIST_METADATA_COLUMNS_LOCAL_TO_SERVER = new HashMap<String, String>();
        TASK_LIST_METADATA_PROPERTIES_EXCLUDED = new HashSet<String>();

        putTaskListMetadataPropertyToServerName(TaskListMetadata.UUID,          "uuid",          false);
        putTaskListMetadataPropertyToServerName(TaskListMetadata.TAG_UUID,      "tag_id",        true);
        putTaskListMetadataPropertyToServerName(TaskListMetadata.FILTER,        "filter",        true);
        putTaskListMetadataPropertyToServerName(TaskListMetadata.TASK_IDS,      "task_ids",      true);
        putTaskListMetadataPropertyToServerName(TaskListMetadata.SORT,          "sort",          false);
        putTaskListMetadataPropertyToServerName(TaskListMetadata.SETTINGS,      "settings",      false);
        putTaskListMetadataPropertyToServerName(TaskListMetadata.CHILD_TAG_IDS, "child_tag_ids", false);
        putTaskListMetadataPropertyToServerName(TaskListMetadata.IS_COLLAPSED,  "is_collapsed",  false);
    }

    // ----------
    // Mapping helpers
    // ----------

    public static boolean shouldRecordOutstandingColumnForTable(String table, String column) {
        if (TABLE_ID_TASKS.equals(table)) {
           if (TASK_COLUMN_NAMES_TO_PROPERTIES.containsKey(column)) {
               return !TASK_PROPERTIES_EXCLUDED.contains(column);
           }
        } else if (TABLE_ID_TAGS.equals(table)) {
            if (TAG_DATA_COLUMN_NAMES_TO_PROPERTIES.containsKey(column)) {
                return !TAG_PROPERTIES_EXCLUDED.contains(column);
            }
        } else if (TABLE_ID_USER_ACTIVITY.equals(table)) {
            if (USER_ACTIVITY_COLUMN_NAMES_TO_PROPERTIES.containsKey(column)) {
                return !USER_ACTIVITY_PROPERTIES_EXCLUDED.contains(column);
            }
        } else if (TABLE_ID_USERS.equals(table)) {
            if (USER_COLUMN_NAMES_TO_PROPERTIES.containsKey(column)) {
                return !USER_PROPERTIES_EXCLUDED.contains(column);
            }
        } else if (TABLE_ID_ATTACHMENTS.equals(table)) {
            if (TASK_ATTACHMENT_COLUMN_NAMES_TO_PROPERTIES.containsKey(column)) {
                return !TASK_ATTACHMENT_PROPERTIES_EXCLUDED.contains(column);
            }
        } else if (TABLE_ID_TASK_LIST_METADATA.equals(table)) {
            if (TASK_LIST_METADATA_COLUMN_NAMES_TO_PROPERTIES.containsKey(column)) {
                return !TASK_LIST_METADATA_PROPERTIES_EXCLUDED.contains(column);
            }
        }
        return false;
    }
}
