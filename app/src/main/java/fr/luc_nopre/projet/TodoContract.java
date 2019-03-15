package fr.luc_nopre.projet;

import android.provider.BaseColumns;


public final class TodoContract {
    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_LABEL = "label";
        public static final String COLUMN_NAME_TAG = "tag";
        public static final String COLUMN_NAME_DONE = "done";
        public static final String COLUMN_NAME_POSITION = "position";
        public static final String COLUMN_NAME_DATE = "dateEcheance";
    }
}
