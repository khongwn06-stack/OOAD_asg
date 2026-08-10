package enums;

/**
 * ============================================================
 * 知识点来源标注 (Knowledge Source Annotation)
 * ============================================================
 * enum（枚举类型）在 LectureNotes 中【完全没有出现】。
 * 只在 ch02a_Basics.pdf 的 Java 关键字表格里列出了 "enum" 这个词
 * （只是关键字列表，没有任何语法说明或例子）。
 *
 * ⚠️ 这是 Assignment 要求 (Section B, "These fixed values must be
 * implemented using enum types.") 但课程讲义完全没有教过的知识点。
 * 这里使用的是 Java 官方标准语法（不是额外的第三方知识），
 * 只是这门课的讲义没有涉及，请自行跟组员/老师确认是否可以使用。
 * ============================================================
 */
public enum TicketType {
    SINGLE,
    DAILY,
    MONTHLY
}
