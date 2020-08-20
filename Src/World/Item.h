#ifndef ITEM_H_INCLUDED
#define ITEM_H_INCLUDED

#include <Mesh/Mesh.h>

class ModelInstance;

class Item {
	private:
		ModelInstance* model;

	protected:
		
		
	public:
		Item(const PGE::FilePath& path, PGE::Graphics* gr);
		
		//virtual void onPickup() = 0;
		//virtual void onDrop() = 0;

		//virtual void update() = 0;
		void render();
};

#endif // ITEM_H_INCLUDED
