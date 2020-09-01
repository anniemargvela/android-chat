package com.example.chatserver.Domain.UseCases.Chats

import com.example.chatserver.Domain.Entities.Chat.Message
import com.example.chatserver.Domain.Repositories.MessagesReposiory
import com.example.chatserver.Domain.UseCases.SingleUseCase
import io.reactivex.Single
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class GetMessagesWIthFriendUseCase @Inject constructor(private val messagesReposiory: MessagesReposiory) :
    SingleUseCase<GetMessagesResponse, GetMessagesWIthFriendUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<GetMessagesResponse> {
        return with(params) {
            var result = GetMessagesResponse(messagesReposiory.get(params.me + params.friend,
                params.skip, params.take), messagesReposiory.count(params.me + params.friend))

            return Single.just(result)
        }
    }

    data class Params(
        val me: String,
        val friend: String,
        val skip: Int,
        val take: Int
    )
}


data class GetMessagesResponse(val messages: List<Message>, val quantity :Int){
    fun ToJson() : String {
        var res = JSONObject()

        res.put("quantity", quantity)

        var messagesJson = JSONArray()
        for(it in messages)
        {
            var item = JSONObject()
            item.put("message",it.value)
            item.put("isSender",it.isSender)
            item.put("date",it.updateDate)
            messagesJson.put(item)
        }

        res.put("messages", messagesJson)

        return  res.toString()
    }
}
