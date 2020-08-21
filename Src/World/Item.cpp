#include "Item.h"

#include "../Models/Model.h"

Item::Item(const PGE::FilePath& path, PGE::Graphics* gr) {

}

void Item::render() {
	model->render();
}
