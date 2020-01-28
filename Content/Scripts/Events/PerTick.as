
funcdef void PerTickEvent(float);

array<PerTickEvent@> perTickEvents;
void RegisterPerTick(PerTickEvent@ event) {
    perTickEvents.insertLast(event);
}

void UnregisterPerTick(PerTickEvent@ event) {
    for (int i=0;i<perTickEvents.length();i++) {
        if (perTickEvents[i] is event) {
            perTickEvents.removeAt(i);
            break;
        }
    }
}

void PerformPerTick(float deltaTime) {
    for (int i=0;i<perTickEvents.length();i++) {
        perTickEvents[i](deltaTime);
    }
}
