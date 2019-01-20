#ifndef OBJECT_H
#define OBJECT_H

#include <vector>

#include "../gxruntime/gxchannel.h"

#include "../gxruntime/StringType.h"

#include "animator.h"
#include "collision.h"

class gxSound;

struct ObjCollision{
	Object *with;
	Vector coords;
	Collision collision;
};

class Object;
class Camera;
class Light;
class Model;
class Listener;
class Pivot;

class Object {
public:
    Object();
    Object(const Object &object);
    ~Object();

    typedef std::vector<const ObjCollision*> Collisions;

	//Object interface
	virtual Object* clone() =0;

	//deep object copy!
	Object *copy();

	//called by user
	void reset();
	void setCollisionType( int type );
	void setCollisionRadii( const Vector &radii );
	void setCollisionBox( const Box &box );
	void setOrder( int n ){ order=n; }
	void setPickGeometry( int n ){ pick_geom=n; }
	void setObscurer( bool t ){ obscurer=t; }
	void setAnimation( const Animation &t ){ anim=t; }
	void setAnimator( Animator *t );

	gxChannel *emitSound( gxSound *sound );

	//overridables!
	virtual bool collide( const Line &line,float radius,::Collision *curr_coll,const Transform &t ){ return false; }
	virtual void capture();
	virtual void animate( float e );
	virtual bool beginRender( float tween );
	virtual void endRender();

	//for use by world
	void beginUpdate( float elapsed );
	void addCollision( const ObjCollision *c );
	void endUpdate();

	//accessors
	int getCollisionType()const;
	const Vector &getCollisionRadii()const;
	const Box &getCollisionBox()const;
	int getOrder()const{ return order; }
	const Vector &getVelocity()const;
	const Collisions &getCollisions()const;
	virtual const Transform &getRenderTform()const;
    const Transform &getBaseRenderTform()const;
	const Transform &getPrevWorldTform()const;
	int getPickGeometry()const{ return pick_geom; }
	int getObscurer()const{ return obscurer; }
	Animation getAnimation()const{ return anim; }
	Animator *getAnimator()const{ return animator; }
	Object *getLastCopy()const{ return last_copy; }

    //ugly casts!
    virtual Camera* getCamera() { return 0; }
    virtual Light* getLight() { return 0; }
    virtual Model* getModel() { return 0; }
    virtual Listener* getListener() { return 0; }
    virtual Pivot* getPivot() { return 0; }

    void setName(const String& t);
    void setParent(Object *parent);

    void setVisible(bool vis);
    void setEnabled(bool ena);

    bool visible()const { return _visible; }
    bool enabled()const { return _enabled; }

    void enumVisible(std::vector<Object*> &out);
    void enumEnabled(std::vector<Object*> &out);

    Object *children()const { return _children; }
    Object *successor()const { return _succ; }

    String getName()const { return _name; }
    Object *getParent()const { return _parent; }

    void setLocalPosition(const Vector &v);
    void setLocalScale(const Vector & v);
    void setLocalRotation(const Quat &q);
    void setLocalTform(const Transform &t);

    void setWorldPosition(const Vector &v);
    void setWorldScale(const Vector &v);
    void setWorldRotation(const Quat &q);
    void setWorldTform(const Transform &t);

    const Vector &getLocalPosition()const;
    const Vector &getLocalScale()const;
    const Quat &getLocalRotation()const;
    const Transform &getLocalTform()const;

    const Vector &getWorldPosition()const;
    const Vector &getWorldScale()const;
    const Quat &getWorldRotation()const;
    const Transform &getWorldTform()const;

    static Object *orphans() { return _orphans; }

protected:
    Object * _succ, *_pred, *_parent, *_children, *_last_child;

    static Object *_orphans, *_last_orphan;

    bool _visible, _enabled;

    String _name;

    mutable int invalid;

    Quat local_rot;
    Vector local_pos, local_scl;
    mutable Transform local_tform;

    mutable Quat world_rot;
    mutable Vector world_pos, world_scl;
    mutable Transform world_tform;

    void insert();
    void remove();
    void invalidateLocal();
    void invalidateWorld();

	int coll_type;
	int order;
	Vector coll_radii;
	Collisions colls;
	bool captured;
	Box coll_box;
	int pick_geom;
	bool obscurer;
	float elapsed;
	Vector velocity;
    std::vector<gxChannel*> channels;
	Vector capt_pos,capt_scl;
	Quat capt_rot;
	mutable Object *last_copy;

	Transform prev_tform;
	Transform captured_tform,tween_tform;
	mutable Transform render_tform;
	mutable bool render_tform_valid;

	Animation anim;
	Animator *animator;

	void updateSounds();
};

#endif
