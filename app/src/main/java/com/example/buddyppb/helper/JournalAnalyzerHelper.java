package com.example.buddyppb.helper;

import android.content.Context;

import com.example.buddyppb.ml.JurnalModel;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class JournalAnalyzerHelper {

    public static float[] journalAnalyzerHelper(Context context, String inputText) {
        ByteBuffer byteBuffer = preprocessTextToByteBuffer(inputText);

        JurnalModel model = null;
        try {
            model = JurnalModel.newInstance(context);

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 44}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            JurnalModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            return outputFeature0.getFloatArray();

        } catch (Exception e) {
            e.printStackTrace();
            return new float[0];

        } finally {
            if (model != null) {
                model.close();
            }
        }
    }

    public static ByteBuffer preprocessTextToByteBuffer(String inputText) {
        byte[] inputBytes = inputText.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(44 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        // Convert each character to float (max 44)
        for (int i = 0; i < inputBytes.length && i < 44; i++) {
            byteBuffer.putFloat((float) inputBytes[i]);
        }

        // Padding remaining space
        while (byteBuffer.position() < 44 * 4) {
            byteBuffer.putFloat(0f);
        }

        byteBuffer.rewind();
        return byteBuffer;
    }
}
