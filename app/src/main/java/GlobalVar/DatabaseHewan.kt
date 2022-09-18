package GlobalVar

import Model.Hewan

class DatabaseHewan {
    companion object {
        val STORAGE_PERMISSION_CODE: Int=3
        val listDataHewan = ArrayList<Hewan>()
        val CAMERA_PERMISSION_CODE = 5
    }
}