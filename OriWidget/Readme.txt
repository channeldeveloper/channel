1. 公用组件包
2. 公用组件放在包com.original.widget.*下面，命名以"O"开头，如OArrowedPanel。
3. 如果组件定了UI类，放在包com.original.widget.plaf.*下面，命名以"O"开头，如OArrowedPanelUI。
4. 组件的event放在com.original.widget.event.*下面，如ArrowChangeEvent，ArrowChangeListener。
5. 组件的model放在com.original.widget.model.*下面，如ArrowModel。
6. 如果组件有比较复杂的处理，可以在com.original.widget下面建立一个package，进行特别管理。