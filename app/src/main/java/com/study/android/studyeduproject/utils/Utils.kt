package com.study.android.studyeduproject.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.text.TextUtils
import android.widget.TextView
import java.util.regex.Pattern

import java.io.ByteArrayOutputStream
import java.text.DecimalFormat

class Utils {


    companion object {


        fun isUseNetwork(context: Context) : Boolean {

            var connectivity : ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

            if(connectivity == null)
                return false

            else{
                var networkInfo : NetworkInfo = connectivity.activeNetworkInfo

                if(networkInfo.state == NetworkInfo.State.CONNECTED)
                    return true
            }

            return false
        }

        /**
         * 비밀번호 유효성 체크
         *
         * @param pw
         * 체크할 비밀번호
         * @return 유효성 여부
         */
        fun isValidPw(pw: String): Boolean {

            // 대문자가 안걸러져서 대문자는 별도로
            if (Pattern.compile("^(.*[A-Z].*).$").matcher(pw).matches()) {
                return false
            }

            val stricterFilterString = "^((?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^*+=-])).{8,16}$"
            val p = java.util.regex.Pattern.compile(stricterFilterString)
            val m = p.matcher(pw)
            return m.matches()
        }

        /**
         * 아이디 유효성 체크
         *
         * @param id
         * 체크할 아이디
         * @return 유효성 여부
         */
        fun isValidId(id: String): Boolean {

            // 대문자가 안걸러져서 대문자는 별도로
            if (Pattern.compile("^(.*[A-Z].*).$").matcher(id).matches()) {
                return false
            }

            val stricterFilterString = "^((?=.*[0-9])(?=.*[a-z])|(?=.*[a-z])).{4,16}$"
            val p = Pattern.compile(stricterFilterString)
            val m = p.matcher(id)
            return m.matches()
        }


        /**
         * 이름 유효성 체크
         * @param nickname 체크할 이름
         * @return 유효성 여부
         */
        fun isValidName(nickname: String): Boolean {
            val stricterFilterString = "^[ㄱ-힣\\s]*$"
            val p = Pattern.compile(stricterFilterString)
            val m = p.matcher(nickname)
            return m.matches()
        }

        /**
         * 이메일 유효성 체크
         *
         * @param email
         * 체크할 이메일
         * @return 유효성 여부
         */
        fun isValidEmailAddress(email: String): Boolean {
            val stricterFilter = true
            val stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
            val laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*"
            val emailRegex = if (stricterFilter) stricterFilterString else laxString
            val p = Pattern.compile(emailRegex)
            val m = p.matcher(email)
            return m.matches()
        }

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {

            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 2

            if (height > reqHeight || width > reqWidth) {

                val halfHeight = height / 2
                val halfWidth = width / 2

                while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }

        fun rotate(ei: ExifInterface, bitmap: Bitmap, quality: Int): Bitmap {
            var bitmap = bitmap

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)

            // 이미지 정보 객체에서 회전에 대한 정보를 추출
            val exifOrientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            // 해당 정보를 기반으로 int 회전각 수치를 추출
            val exifDegree = exifOrientationToDegrees(exifOrientation)

            if (exifDegree != 0 && bitmap != null) {
                val m = Matrix()

                // 회전각을 적용 시키고 일단은 해당 사진 크기의 절반 정도 크기로 줄인다
                m.setRotate(exifDegree.toFloat(), bitmap.width.toFloat(), bitmap.height.toFloat())

                try {
                    // 회전 !
                    val converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)

                    if (bitmap != converted) {
                        bitmap.recycle()
                        bitmap = converted
                    }
                } catch (ex: OutOfMemoryError) {
                    // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환.
                    ex.printStackTrace()
                }

            }
            return bitmap
        }

        fun exifOrientationToDegrees(exifOrientation: Int): Int {
            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270
            }
            return 0
        }

        fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                // url에 _data 컬럼 데이터를 전체 가져온다
                cursor = context.contentResolver.query(contentUri, projection, null, null, null)

                //커서를 처음으로 이동시키고
                if (cursor != null && cursor.moveToFirst()) {
                    // 지정한 컬럼 인덱스 가져온 뒤
                    val index = cursor.getColumnIndexOrThrow(column)
                    // 해당하는 정보를 string으로 반환
                    return cursor.getString(index)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
            return null
        }


        /**
         * 텍스트뷰의 넓이를 계산해서 텍스트가 넓이에 딱 맞게 문자단위로 자르도록
         * 해주는 함수
         * @param text 자를 텍스트
         * @param textview 적용될 텍스트 뷰
         * @return 공백 새로 고친 텍스트
         */
        fun getWordUnitMultiLine(text: String, textview: TextView?): String {
            if (TextUtils.isEmpty(text))
                return text
            if (textview == null)
                return text
            val paint = textview.paint ?: return text

            paint.color = textview.textColors.defaultColor
            paint.textSize = textview.textSize

            val measureWidth = textview.measuredWidth
            val paddingRight = textview.paddingRight
            val paddingLeft = textview.paddingLeft

            if (measureWidth <= 0)
                return text

            val nAvailableWidth = measureWidth + paddingRight + paddingLeft
            var copyText = text
            var end = 0
            val sb = StringBuilder()
            do {
                end = paint.breakText(copyText, true, nAvailableWidth.toFloat(), null)
                if (end > 0) {
                    sb.append(copyText.substring(0, end))
                    copyText = copyText.substring(end)
                }
                if (end != 0 && sb.toString().length != text.length)
                    sb.append("\n")
            } while (end > 0)
            return if (TextUtils.isEmpty(sb.toString())) text else sb.toString()
        }
    }

}
