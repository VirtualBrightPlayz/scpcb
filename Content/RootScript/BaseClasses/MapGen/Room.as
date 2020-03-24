shared abstract class Room {
    protected nonserialize RM2@ mesh;
    protected nonserialize Zone@ zone;

    protected nonserialize Vector3f _position;
    Vector3f position {
        get {
            return _position;
        }
        set {
            _position = value;
            recalculateWorldMatrix();
        }
    }
    protected nonserialize float _rotation;
    float rotation {
        get {
            return _rotation;
        }
        set {
            _rotation = value;
            recalculateWorldMatrix();
        }
    }

    protected nonserialize Matrix4x4f worldMatrix;

    protected void recalculateWorldMatrix() {
        worldMatrix = Matrix4x4f::constructWorldMat(position, Vector3f(0.1, 0.1, 0.1), Vector3f(0.0, rotation, 0.0));
    }

    void update(float deltaTime) {}

    void render(float interpolation) {
        mesh.render(worldMatrix);
    }
}
