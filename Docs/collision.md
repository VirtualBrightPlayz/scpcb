# Collision

----

## Class Collision::Mesh

Used to represent meshes.

### Constructor Collision::Mesh(array<Vector3f> verts, array<int> inds)

Constructs a mesh using the given vertices and indices.

**Parameters**
* `verts` The points in 3D space of the corners of the mesh.
* `inds` The indices of the mesh.

----

## Collision::Collection Class

A collection of meshes. Parameterless constructor.

### Collision::Collection.addInstance(Mesh mesh, Matrix4x4f matrix)

Adds a `Mesh` to the `Collection`.

**Parameters**
* `mesh` The mesh to add.
* `matrix` A matrix representing the rotation, scale and translation to apply when performing collision checks.

**Returns**
* The `Instance` of the `Mesh` inside the `Collection` (?)