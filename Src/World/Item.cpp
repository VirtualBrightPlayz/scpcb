#include "Item.h"

#include "../Models/Generic.h"

Item::Item(const PGE::FilePath& path, PGE::Graphics* gr) {

}

void Item::render() {
	model->render();
}
