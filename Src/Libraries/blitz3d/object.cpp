#include <vector>

#include "../gxruntime/gxruntime.h"

#include "object.h"

extern gxRuntime *gx_runtime;

enum {
    INVALID_LOCALTFORM = 1,
    INVALID_WORLDTFORM = 2
};

Object::Object():
_succ(0), _pred(0), _parent(0), _children(0), _last_child(0),
_visible(true), _enabled(true),
local_scl(1, 1, 1),
invalid(0),
order(0),animator(0),last_copy(0),
coll_type(0),coll_radii(Vector(1,1,1)),coll_box(Box(Vector(-1,-1,-1),Vector(1,1,1))),
pick_geom(0),obscurer(false),captured(false){
    insert();
	reset();
}

Object::Object( const Object &o ):
_succ(0), _pred(0), _parent(0), _children(0), _last_child(0),
_name(o._name), _visible(o._visible), _enabled(o._enabled),
local_pos(o.local_pos),
local_scl(o.local_scl),
local_rot(o.local_rot),
invalid(INVALID_LOCALTFORM | INVALID_WORLDTFORM),
order(o.order),animator(0),last_copy(0),
coll_type(o.coll_type),coll_radii(o.coll_radii),coll_box(o.coll_box),
pick_geom(o.pick_geom),obscurer(o.obscurer),captured(false){
    insert();
	reset();
}

Object::~Object(){
	delete animator;
	velocity=Vector();
	updateSounds();

    while (children()) delete children();
    remove();
}

Object *Object::copy(){
	last_copy=clone();
	for( Object *e=children();e;e=e->successor() ){
		Object *cpy=e->copy();
		cpy->setParent( last_copy );
	}
	if( animator ) last_copy->setAnimator( new Animator( animator ) );
	return last_copy;
}

void Object::reset(){
	colls.clear();
	velocity=Vector();
	prev_tform=getWorldTform();
}

void Object::setCollisionType( int type ){
	coll_type=type;
}

void Object::setCollisionRadii( const Vector &radii ){
	coll_radii=radii;
}

void Object::setCollisionBox( const Box &box ){
	coll_box=box;
}

void Object::setAnimator( Animator *t ){
	if( animator ) delete animator;
	animator=t;
}

void Object::beginUpdate( float e ){
	elapsed=e;
	colls.clear();
	animate( e );
}

void Object::animate( float e ){
	if( animator ) animator->update( e );
}

void Object::addCollision( const ObjCollision *c ){
	colls.push_back( c );
}

void Object::endUpdate(){
	velocity=(getWorldTform().v-prev_tform.v)/elapsed;
	prev_tform=getWorldTform();
}

void Object::capture(){
	capt_pos=getLocalPosition();
	capt_scl=getLocalScale();
	capt_rot=getLocalRotation();
	captured=true;
}

bool Object::beginRender( float tween ){
	updateSounds();
	if( tween==1 || !captured ){
		render_tform=getWorldTform();
		render_tform_valid=true;
	}else{
		Vector pos=(getLocalPosition()-capt_pos)*tween+capt_pos;
		Vector scl=(getLocalScale()-capt_scl)*tween+capt_scl;
		Quat rot=capt_rot.slerpTo( getLocalRotation(),tween );
		tween_tform.m=Matrix( rot );
		tween_tform.m.i*=scl.x;
		tween_tform.m.j*=scl.y;
		tween_tform.m.k*=scl.z;
		tween_tform.v=pos;
		render_tform_valid=false;
	}
	return true;
}

void Object::endRender(){
}

int Object::getCollisionType()const{
	return coll_type;
}

const Vector &Object::getCollisionRadii()const{
	return coll_radii;
}

const Box &Object::getCollisionBox()const{
	return coll_box;
}

const Vector &Object::getVelocity()const{
	return velocity;
}

const Object::Collisions &Object::getCollisions()const{
	return colls;
}

const Transform &Object::getRenderTform()const {
    return getBaseRenderTform();
}

const Transform &Object::getBaseRenderTform()const{
	if( render_tform_valid ) return render_tform;

	Object *parent=(Object*)getParent();
	render_tform=parent ? parent->getBaseRenderTform() * tween_tform : tween_tform;
	render_tform_valid=true;

	return render_tform;
}

const Transform &Object::getPrevWorldTform()const{
	return prev_tform;
}

gxChannel *Object::emitSound( gxSound *sound ){
	if( !sound ) return 0;

	gxChannel *chan=nullptr;//sound->play3d( &getWorldTform().v.x,&velocity.x );
	for( int k=0;k<channels.size();++k ){
		if( chan==channels[k] ) return chan;
		if( !channels[k] ) return channels[k]=chan;
	}
	channels.push_back( chan );
	return chan;
}

void Object::updateSounds(){
	for( int k=0;k<channels.size();++k ){
		if( gxChannel *chan=channels[k] ){
			if( chan->isPlaying() )	chan->set3d( &getWorldTform().v.x,&velocity.x );
			else channels[k]=0;
		}
	}
}

Object *Object::_orphans, *Object::_last_orphan;

void Object::remove() {
    if (_parent) {
        if (_parent->_children == this) _parent->_children = _succ;
        if (_parent->_last_child == this) _parent->_last_child = _pred;
    }
    else {
        if (_orphans == this) _orphans = _succ;
        if (_last_orphan == this) _last_orphan = _pred;
    }
    if (_succ) _succ->_pred = _pred;
    if (_pred) _pred->_succ = _succ;
}

void Object::insert() {
    _succ = 0;
    if (_parent) {
        if (_pred = _parent->_last_child) _pred->_succ = this;
        else _parent->_children = this;
        _parent->_last_child = this;
    }
    else {
        if (_pred = _last_orphan) _pred->_succ = this;
        else _orphans = this;
        _last_orphan = this;
    }
}

void Object::invalidateWorld() {
    if (invalid & INVALID_WORLDTFORM) return;
    invalid |= INVALID_WORLDTFORM;
    for (Object *e = _children; e; e = e->_succ) {
        e->invalidateWorld();
    }
}

void Object::invalidateLocal() {
    invalid |= INVALID_LOCALTFORM;
    invalidateWorld();
}

const Transform &Object::getLocalTform()const {
    if (invalid&INVALID_LOCALTFORM) {
        local_tform.m = Matrix(local_rot);
        local_tform.m.i *= local_scl.x;
        local_tform.m.j *= local_scl.y;
        local_tform.m.k *= local_scl.z;
        local_tform.v = local_pos;
        invalid &= ~INVALID_LOCALTFORM;
    }
    return local_tform;
}

const Transform &Object::getWorldTform()const {
    if (invalid&INVALID_WORLDTFORM) {
        world_tform = _parent ? _parent->getWorldTform() * getLocalTform() : getLocalTform();
        invalid &= ~INVALID_WORLDTFORM;
    }
    return world_tform;
}

void Object::setParent(Object *p) {
    if (_parent == p) return;

    remove();

    _parent = p;

    insert();

    invalidateWorld();
}

void Object::setName(const String& t) {
    _name = t;
}

void Object::setVisible(bool visible) {
    _visible = visible;
}

void Object::setEnabled(bool enabled) {
    _enabled = enabled;
}

void Object::enumVisible(std::vector<Object*> &out) {
    if (!_visible) return;
    if (Object *o = this) out.push_back(o);
    for (Object *e = _children; e; e = e->_succ) {
        e->enumVisible(out);
    }
}

void Object::enumEnabled(std::vector<Object*> &out) {
    if (!_enabled) return;
    if (Object *o = this) out.push_back(o);
    for (Object *e = _children; e; e = e->_succ) {
        e->enumEnabled(out);
    }
}

void Object::setLocalPosition(const Vector &v) {
    local_pos = v;
    invalidateLocal();
}

void Object::setLocalScale(const Vector &v) {
    local_scl = v;
    invalidateLocal();
}

void Object::setLocalRotation(const Quat &q) {
    local_rot = q.normalized();
    invalidateLocal();
}

void Object::setLocalTform(const Transform &t) {
    local_pos = t.v;
    local_scl = Vector(t.m.i.length(), t.m.j.length(), t.m.k.length());
    local_rot = matrixQuat(t.m);
    invalidateLocal();
}

void Object::setWorldPosition(const Vector &v) {
    setLocalPosition(_parent ? -_parent->getWorldTform() * v : v);
}

void Object::setWorldScale(const Vector &v) {
    setLocalScale(_parent ? v / _parent->getWorldScale() : v);
}

void Object::setWorldRotation(const Quat &q) {
    setLocalRotation(_parent ? -_parent->getWorldRotation() * q : q);
}

void Object::setWorldTform(const Transform &t) {
    setLocalTform(_parent ? -_parent->getWorldTform() * t : t);
}

const Vector &Object::getLocalPosition()const {
    return local_pos;
}

const Vector &Object::getLocalScale()const {
    return local_scl;
}

const Quat &Object::getLocalRotation()const {
    return local_rot;
}

const Vector &Object::getWorldPosition()const {
    return getWorldTform().v;
}

const Vector &Object::getWorldScale()const {
    world_scl = _parent ? _parent->getWorldScale() * local_scl : local_scl;
    return world_scl;
}

const Quat &Object::getWorldRotation()const {
    world_rot = _parent ? _parent->getWorldRotation() * local_rot : local_rot;
    return world_rot;
}
