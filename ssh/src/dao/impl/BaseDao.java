package dao.impl;

import model.HasSchoolSchema;

public class BaseDao implements HasSchoolSchema {
    protected final String schoolSchema;

    protected BaseDao(String schoolSchema) {
        this.schoolSchema = schoolSchema;
    }

    protected BaseDao(HasSchoolSchema hasSchoolSchemaObject) {
        this(hasSchoolSchemaObject.getSchoolSchema());
    }

    @Override
    public String getSchoolSchema() {
        return this.schoolSchema;
    }

}
