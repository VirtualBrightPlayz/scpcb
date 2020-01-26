array<array<string>> generateLayout(int dimensions) {
    array<array<string>> layout(dimensions);
    for (int i=0;i<dimensions;i++) {
        layout[i] = array<string>(dimensions);
        for (int j=0;j<dimensions;j++) {
            layout[i][j] = "a";
        }
    }

    return layout;
}
